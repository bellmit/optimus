package com.optimus.service.order.core.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.result.MemberInfoForRecursionResult;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.AccountInfoDTO;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeInputDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
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
    private OrderInfoDao orderInfoDao;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // 验证码商余额是否充足
        MemberTransConfineDTO memberTransConfine = checkCodeBalance(createOrder);

        // 执行脚本
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(OrderManagerConvert.getExecuteScriptInputDTO(createOrder));

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
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, createOrder, "下单扣减余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_PLUS, createOrder, "下单增加冻结户"));

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

        // 验证订单状态[只能为成功]
        String orderStatus = payOrder.getOrderStatus();
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), orderStatus, RespCodeEnum.ORDER_ERROR, "订单状态只能为成功");

        // 查询关系链
        List<MemberInfoForRecursionResult> memberInfoList = memberManager.listMemberInfoForRecursions(payOrder.getCodeMemberId());
        AssertUtil.notEmpty(memberInfoList, RespCodeEnum.MEMBER_ERROR, "会员关系链为空");

        // 更新订单状态
        String originOrderStatus = updateOrderStatus(payOrder.getOrderId(), orderStatus);
        AssertUtil.notEmpty(originOrderStatus, RespCodeEnum.ORDER_ERROR, "更新订单状态异常");

        // 构建记账信息
        List<DoTransDTO> doTransList = buildDoTransList(memberInfoList, payOrder, originOrderStatus);
        AssertUtil.notEmpty(doTransList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "构建记账信息异常");

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), originOrderStatus, orderStatus, DateUtil.currentDate());
            return;
        }

        // 异步通知商户
        OrderNoticeInputDTO input = OrderManagerConvert.getOrderNoticeInputDTO(payOrder);
        orderManager.orderNotice(input, payOrder.getMemberInfo().getMemberKey(), payOrder.getMerchantCallbackUrl());

    }

    /**
     * 验证码商余额是否充足
     * 
     * @param createOrder
     * @return
     */
    private MemberTransConfineDTO checkCodeBalance(CreateOrderDTO createOrder) {

        String codeMemberId = createOrder.getCodeMemberId();
        String channelGroup = createOrder.getGatewayChannel().getChannelGroup();
        BigDecimal orderAmount = createOrder.getOrderAmount();

        // 查询码商会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(codeMemberId);
        AssertUtil.notEmpty(memberTransConfine.getCodeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "码商余额限制开关未配置");

        // 自研渠道不验证码商余额
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), channelGroup)) {
            return memberTransConfine;
        }

        // 不限制码商余额
        if (StringUtils.pathEquals(MemberCodeBalanceSwitchEnum.CODE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getCodeBalanceSwitch())) {
            return memberTransConfine;
        }

        // 查询码商余额
        AccountInfoDTO accountInfo = accountManager.getAccountInfoByMemberIdAndAccountType(codeMemberId, AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
        if (accountInfo.getAmount().compareTo(orderAmount) < 0) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
        }

        return memberTransConfine;

    }

    /**
     * 更新订单状态
     * 
     * @param orderId
     * @param orderStatus
     * @return
     */
    private String updateOrderStatus(String orderId, String orderStatus) {

        // 更新结果
        int update = 0;

        // 默认原状态等待支付
        String originOrderStatus = OrderStatusEnum.ORDER_STATUS_NP.getCode();

        // 由等待支付更新订单状态
        update = orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(orderId, orderStatus, originOrderStatus, DateUtil.currentDate());
        if (update == 1) {
            return originOrderStatus;
        }

        // 更新原状态为订单挂起
        originOrderStatus = OrderStatusEnum.ORDER_STATUS_NP.getCode();

        // 由订单挂起状态更新订单状态
        update = orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(orderId, orderStatus, originOrderStatus, DateUtil.currentDate());
        if (update == 1) {
            return originOrderStatus;
        }

        return null;

    }

    /**
     * 构建记账信息
     * 
     * @param memberInfoList
     * @param payOrder
     * @param originOrderStatus
     * @return
     */
    private List<DoTransDTO> buildDoTransList(List<MemberInfoForRecursionResult> memberInfoList, PayOrderDTO payOrder, String originOrderStatus) {

        List<DoTransDTO> doTransList = new ArrayList<>();

        return doTransList;

    }

}
