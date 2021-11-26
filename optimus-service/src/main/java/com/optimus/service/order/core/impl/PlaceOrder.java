package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.AccountInfoDTO;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeInputDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.manager.order.validate.OrderManagerValidate;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.member.MemberCodeBalanceSwitchEnum;
import com.optimus.util.constants.member.MemberFreezeBalanceSwitchEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

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

        // 查询码商会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(createOrder.getCodeMemberId());
        AssertUtil.notEmpty(memberTransConfine.getCodeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "码商余额限制开关未配置");

        // 验证码商余额:非自研渠道且非关闭
        if (!StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())
                && !StringUtils.pathEquals(MemberCodeBalanceSwitchEnum.CODE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getCodeBalanceSwitch())) {
            // 查询账户信息
            AccountInfoDTO accountInfo = accountManager.getAccountInfoByMemberIdAndAccountType(createOrder.getCodeMemberId(), AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
            if (accountInfo.getAmount().compareTo(createOrder.getOrderAmount()) < 0) {
                throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
            }
        }

        // 执行脚本
        ExecuteScriptInputDTO executeScriptInput = OrderManagerConvert.getExecuteScriptInputDTO(createOrder);
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(executeScriptInput);

        // 订单信息DTO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder, output);

        // 验证下单状态
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            return orderInfo;
        }

        // 不冻结码商余额
        if (StringUtils.pathEquals(MemberFreezeBalanceSwitchEnum.FREEZE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getFreezeBalanceSwitch())) {
            return orderInfo;
        }

        // 赋值:码商会员编号和实际金额
        createOrder.setMemberId(orderInfo.getCodeMemberId());
        createOrder.setActualAmount(orderInfo.getActualAmount());

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, createOrder, "下单减余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_PLUS, createOrder, "下单加冻结户"));

        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
        }

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
        AssertUtil.notEmpty(chainList, RespCodeEnum.MEMBER_ERROR, "会员信息链为空");

        // 查询会员关系链的会员渠道费率
        List<String> memberIdList = chainList.stream().map(MemberInfoChainResult::getMemberId).collect(Collectors.toList());
        memberIdList.add(payOrder.getMemberId());
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdLists(memberIdList);

        // 验证会员信息链渠道费率
        OrderManagerValidate.validateMemberChain(chainList, memberChannelList);

        // 更新订单状态:原状态可能为等待支付或订单挂起
        String originOrderStatus = OrderStatusEnum.ORDER_STATUS_NP.getCode();
        int update = orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), payOrder.getOrderStatus(), originOrderStatus, DateUtil.currentDate());
        if (update != 1) {
            originOrderStatus = OrderStatusEnum.ORDER_STATUS_HU.getCode();
            update = orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), payOrder.getOrderStatus(), originOrderStatus, DateUtil.currentDate());
        }
        if (update != 1) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR, "更新订单状态异常");
        }

        // 构建记账信息
        List<DoTransDTO> doTransList = OrderManagerConvert.getDoTransDTOList(chainList, memberChannelList, payOrder, originOrderStatus);
        AssertUtil.notEmpty(doTransList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "构建记账信息异常");

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), originOrderStatus, payOrder.getOrderStatus(), DateUtil.currentDate());
            return;
        }

        // 异步通知商户
        OrderNoticeInputDTO input = OrderManagerConvert.getOrderNoticeInputDTO(payOrder);
        orderManager.orderNotice(input, payOrder.getMerchantCallbackUrl());

    }

}
