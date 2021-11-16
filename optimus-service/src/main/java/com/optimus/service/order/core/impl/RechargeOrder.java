package com.optimus.service.order.core.impl;

import com.optimus.service.order.convert.OrderServiceConvert;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;

import org.springframework.stereotype.Component;

/**
 * 充值
 *
 * @author hongp
 */
@Component
public class RechargeOrder extends BaseOrder {

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {
        OrderInfoDTO orderInfo = OrderServiceConvert.getOrderInfoDTO(createOrder);
        orderInfo.setActualAmount(createOrder.getOrderAmount());
        return orderInfo;
    }

    /**
     * 支付订单
     *
     * @param payOrder
     */
    @Override
    public void payOrder(PayOrderDTO payOrder) {

    }

}
