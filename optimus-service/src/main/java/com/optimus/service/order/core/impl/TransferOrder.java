package com.optimus.service.order.core.impl;

import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;

import org.springframework.stereotype.Component;

/**
 * 划账
 *
 * @author hongp
 */
@Component
public class TransferOrder extends BaseOrder {

    /**
     * 创建订单
     *
     * @param createOrder
     * @return
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {
        return null;
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
