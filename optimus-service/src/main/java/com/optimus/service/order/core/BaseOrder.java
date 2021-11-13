package com.optimus.service.order.core;

import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.PayOrderDTO;

/**
 * 订单处理父类
 *
 * @author hongp
 */
public abstract class BaseOrder {

    /**
     * 创建订单
     *
     * @param createOrder
     */
    public abstract void createOrder(CreateOrderDTO createOrder);

    /**
     * 支付订单
     *
     * @param payOrder
     */
    public abstract void payOrder(PayOrderDTO payOrder);

}
