package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.common.CommonSystemConfigTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.member.MemberCodeBalanceSwitchEnum;
import com.optimus.util.constants.member.MemberFreezeBalanceSwitchEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * ??????
 *
 * @author hongp
 */
@Component
@Slf4j
public class PlaceOrder extends BaseOrder {

    @Autowired
    private CommonSystemConfigManager commonSystemConfigManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private GatewayManager gatewayManager;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private CommonSystemConfigDao commonSystemConfigDao;

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private OrderInfoDao orderInfoDao;

    /**
     * ????????????
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // ????????????????????????:??????
        MemberTransConfineDTO memberTransConfine = checkMemberTrans(createOrder);

        // ????????????????????????
        String value = commonSystemConfigManager.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_CALLBACK_DOMAIN.getCode());
        AssertUtil.notEmpty(value, RespCodeEnum.ERROR_CONFIG, "?????????????????????????????????");
        String[] values = value.split(",");
        value = values[ThreadLocalRandom.current().nextInt(values.length)];

        // ????????????
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(OrderManagerConvert.getExecuteScriptInputDTO(createOrder, value));
        AssertUtil.notEmpty(output, RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "????????????????????????????????????");

        // ????????????
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder, output);
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            log.warn("????????????,????????????:{}", orderInfo);
            return orderInfo;
        }

        // ??????????????????:??????
        memberTransConfine = checkMemberTrans(createOrder, memberTransConfine, orderInfo.getCodeMemberId());

        // ?????????????????????
        if (StringUtils.pathEquals(MemberFreezeBalanceSwitchEnum.FREEZE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getFreezeBalanceSwitch())) {
            orderInfo.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode());
            return orderInfo;
        }

        // ??????List
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransForCode(AccountChangeTypeEnum.B_MINUS, orderInfo, "??????????????????"));
        doTransList.add(OrderManagerConvert.getDoTransForCode(AccountChangeTypeEnum.F_PLUS, orderInfo, "??????????????????"));

        // ??????
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            log.warn("????????????????????????,????????????:{}", orderInfo);
            return orderInfo;
        }

        orderInfo.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode());
        return orderInfo;
    }

    /**
     * ????????????
     *
     * @param payOrder
     */
    @Override
    public void payOrder(PayOrderDTO payOrder) {

        // ??????????????????DTO???DO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(payOrder);
        OrderInfoDO orderInfoDO = OrderManagerConvert.getOrderInfoDO(payOrder, OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N);

        // ??????????????????
        int update = orderInfoDao.updateOrderInfoByIdAndOrderStatus(orderInfoDO, OrderStatusEnum.ORDER_STATUS_NP.getCode());
        if (update != 1) {
            return;
        }

        // ??????????????????
        orderManager.asyncRelease(orderInfo);

        // ????????????
        orderManager.asyncSplitProfit(orderInfo);

        // ??????????????????
        orderManager.asyncOrderNotice(orderInfo);

    }

    /**
     * ??????????????????:??????
     * 
     * @param createOrder
     * @return
     */
    private MemberTransConfineDTO checkMemberTrans(CreateOrderDTO createOrder) {

        // ?????????
        if (!StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_O.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            return null;
        }

        // ??????????????????????????????
        MemberTransConfineDTO memberTransConfine = getMemberTransConfine(createOrder.getCodeMemberId(), createOrder.getOrganizeId());

        // ??????????????????????????????
        if (!StringUtils.pathEquals(MemberCodeBalanceSwitchEnum.CODE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getCodeBalanceSwitch())) {
            super.checkAccountAmount(createOrder.getCodeMemberId(), createOrder.getOrderAmount(), AccountTypeEnum.ACCOUNT_TYPE_B);
        }

        return memberTransConfine;

    }

    /**
     * ??????????????????:??????
     * 
     * @param createOrder
     * @param memberTransConfine
     * @param codeMemberId
     * @return
     */
    private MemberTransConfineDTO checkMemberTrans(CreateOrderDTO createOrder, MemberTransConfineDTO memberTransConfine, String codeMemberId) {

        // ?????????
        if (!StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            return memberTransConfine;
        }

        // ??????????????????????????????
        return getMemberTransConfine(codeMemberId, createOrder.getOrganizeId());

    }

    /**
     * ????????????????????????
     * 
     * @param codeMemberId
     * @param organizeId
     * @return
     */
    private MemberTransConfineDTO getMemberTransConfine(String codeMemberId, Long organizeId) {

        // ??????????????????????????????
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(codeMemberId);
        AssertUtil.notEmpty(memberTransConfine, RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "?????????????????????????????????");

        // ?????????:??????????????????????????????
        if (!StringUtils.hasLength(memberTransConfine.getCodeBalanceSwitch()) || !StringUtils.hasLength(memberTransConfine.getFreezeBalanceSwitch())
                || Objects.isNull(memberTransConfine.getReleaseFreezeBalanceAging())) {

            CommonSystemConfigDO commonSystemConfig = commonSystemConfigDao.getCommonSystemConfigByTypeAndBaseKey(CommonSystemConfigTypeEnum.TYPE_MTC.getCode(), String.valueOf(organizeId));
            AssertUtil.notEmpty(commonSystemConfig, RespCodeEnum.ERROR_CONFIG, "???????????????????????????????????????");

            memberTransConfine = JacksonUtil.toBean(commonSystemConfig.getValue(), MemberTransConfineDTO.class);
            memberTransConfine.setMemberId(codeMemberId);
        }

        // ??????:??????
        AssertUtil.notEmpty(memberTransConfine.getCodeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "?????????????????????????????????");
        AssertUtil.notEmpty(memberTransConfine.getFreezeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "???????????????????????????????????????");
        AssertUtil.notEmpty(memberTransConfine.getReleaseFreezeBalanceAging(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "?????????????????????????????????");

        return memberTransConfine;

    }

}
