package com.optimus.service.order.core.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.manager.order.validate.OrderManagerValidate;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.member.MemberCodeBalanceSwitchEnum;
import com.optimus.util.constants.member.MemberFreezeBalanceSwitchEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 下单
 *
 * @author hongp
 */
@Component
public class PlaceOrder extends BaseOrder {

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private GatewayManager gatewayManager;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private OrderInfoDao orderInfoDao;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // 会员交易限制
        MemberTransConfineDTO memberTransConfine = null;

        // 验证会员交易:外部
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_O.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            memberTransConfine = checkMemberTrans(createOrder.getCodeMemberId(), createOrder.getOrderAmount());
        }

        // 执行脚本
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(OrderManagerConvert.getExecuteScriptInputDTO(createOrder));

        // 订单信息
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder, output);
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        // 验证会员交易:自研
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            memberTransConfine = checkMemberTrans(createOrder.getCodeMemberId());
        }

        // 不冻结码商余额
        if (StringUtils.pathEquals(MemberFreezeBalanceSwitchEnum.FREEZE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getFreezeBalanceSwitch())) {
            orderInfo.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode());
            return orderInfo;
        }

        // 记账List
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransForCode(AccountChangeTypeEnum.B_MINUS, orderInfo, "下单减余额户"));
        doTransList.add(OrderManagerConvert.getDoTransForCode(AccountChangeTypeEnum.F_PLUS, orderInfo, "下单加冻结户"));

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        orderInfo.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode());
        return orderInfo;
    }

    /**
     * 支付订单
     *
     * @param payOrder
     */
    @Override
    public void payOrder(PayOrderDTO payOrder) {

        // 查询会员信息链
        List<MemberInfoChainResult> chainList = memberManager.listMemberInfoChains(payOrder.getCodeMemberId());
        AssertUtil.notEmpty(chainList, RespCodeEnum.MEMBER_ERROR, "会员信息链不能为空");

        // 查询会员关系链的会员渠道费率
        List<String> memberIdList = chainList.stream().map(MemberInfoChainResult::getMemberId).collect(Collectors.toList());
        memberIdList.add(payOrder.getMemberId());
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdLists(memberIdList);

        // 验证链及渠道
        OrderManagerValidate.validateChainAndChannel(chainList, memberChannelList, payOrder.getCodeMemberId());

        // 获取订单信息DTO及DO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(payOrder);
        OrderInfoDO orderInfoDO = OrderManagerConvert.getOrderInfoDO(payOrder, OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode());

        // 更新订单状态
        int update = orderInfoDao.updateOrderInfoByIdAndOrderStatus(orderInfoDO, OrderStatusEnum.ORDER_STATUS_NP.getCode());
        if (update != 1) {
            return;
        }

        // 异步释放订单
        orderManager.asyncRelease(orderInfo);

        // 异步分润
        orderManager.asyncSplitProfit(orderInfo, chainList, memberChannelList);

        // 异步通知商户
        orderManager.asyncOrderNotice(orderInfo);

    }

    /**
     * 验证会员交易:外部
     * 
     * @param codeMemberId
     * @param orderAmount
     * @return
     */
    private MemberTransConfineDTO checkMemberTrans(String codeMemberId, BigDecimal orderAmount) {

        // 查询码商会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(codeMemberId);
        AssertUtil.notEmpty(memberTransConfine.getCodeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置码商余额限制开关");
        AssertUtil.notEmpty(memberTransConfine.getReleaseFreezeBalanceAging(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置释放冻结余额时效");

        // 验证账户金额是否充足
        if (!StringUtils.pathEquals(MemberCodeBalanceSwitchEnum.CODE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getCodeBalanceSwitch())) {
            super.checkAccountAmount(codeMemberId, orderAmount, AccountTypeEnum.ACCOUNT_TYPE_B);
        }

        return memberTransConfine;

    }

    /**
     * 验证会员交易:自研
     * 
     * @param codeMemberId
     * @return
     */
    private MemberTransConfineDTO checkMemberTrans(String codeMemberId) {

        // 查询码商会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(codeMemberId);
        AssertUtil.notEmpty(memberTransConfine.getFreezeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置码商订单释放状态开关");
        AssertUtil.notEmpty(memberTransConfine.getReleaseFreezeBalanceAging(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置释放冻结余额时效");

        return memberTransConfine;

    }

}
