package com.optimus.service.order.core;

import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.exception.OptimusException;

/**
 * 订单处理父类
 *
 * @author hongp
 */
public abstract class BaseOrder {

    /**
     * 创建订单
     *
     * @param payOrder
     * @throws OptimusException
     */
    public abstract void createOrder(PayOrderDTO payOrder) throws OptimusException;

    /**
     * 支付订单
     *
     * @param payOrder
     * @throws OptimusException
     */
    public abstract void payOrder(PayOrderDTO payOrder) throws OptimusException;
}
