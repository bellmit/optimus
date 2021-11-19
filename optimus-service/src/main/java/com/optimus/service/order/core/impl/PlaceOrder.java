package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.service.order.convert.OrderServiceConvert;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
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
    private AccountManager accountManager;

    @Autowired
    private GatewayManager gatewayManager;

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

        // 执行脚本
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(OrderServiceConvert.getExecuteScriptInputDTO(createOrder));

        // 获取OrderInfoDTO
        OrderInfoDTO orderInfo = OrderServiceConvert.getOrderInfoDTO(createOrder);
        orderInfo.setCalleeOrderId(output.getCalleeOrderId());
        orderInfo.setOrderStatus(output.getOrderStatus());
        orderInfo.setActualAmount(output.getActualAmount());
        orderInfo.setChannelReturnMessage(output.getChannelReturnMessage());

        // 下单成功状态为等待支付
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        // 自研渠道获取渠道会员编号
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            orderInfo.setCodeMemberId(output.getCodeMemberId());
        }
        if (!StringUtils.hasLength(orderInfo.getCodeMemberId())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        // 明确不冻结码商余额
        if (StringUtils.pathEquals(MemberFreezeBalanceSwitchEnum.FREEZE_BALANCE_SWITCH_N.getCode(), createOrder.getMemberTransConfine().getFreezeBalanceSwitch())) {
            return orderInfo;
        }

        // 注意:记账前修改createOrder相关属性值,后续不可再用orderInfo对象做持久化
        createOrder.setMemberId(orderInfo.getCodeMemberId());
        createOrder.setActualAmount(orderInfo.getActualAmount());

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        OrderServiceConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, createOrder, "下单扣减余额户");
        OrderServiceConvert.getDoTransDTO(AccountChangeTypeEnum.F_PLUS, createOrder, "下单增加冻结户");

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

        // 验证订单状态[只能为成功或失败]
        String orderStatus = payOrder.getOrderStatus();
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), orderStatus) && !StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AF.getCode(), orderStatus)) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR, "订单状态只能为成功或失败");
        }

        // 查询关系链

        // 释放冻结余额标识
        // boolean flag = true;

        // 更新订单状态
        int update = orderInfoDao.updateStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), orderStatus, OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
        if (update != 1) {
            // 无需释放冻结余额
            // flag = false;
            // 使用订单挂起状态再次更新
            update = orderInfoDao.updateStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), orderStatus, OrderStatusEnum.ORDER_STATUS_HU.getCode(), DateUtil.currentDate());
        }

        if (update != 1) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR, "订单状态异常");
        }

        // 异步分润

        // 异步通知商户

    }

}
