package com.optimus.service.order.core.impl;

import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.PayOrderDTO;

import org.springframework.stereotype.Component;

/**
 * 提现
 *
 * @author hongp
 */
@Component
public class WithdrawOrder extends BaseOrder {

    /**
     * 创建订单
     *
     * @param payOrder
     */
    @Override
    public void createOrder(PayOrderDTO payOrder) {

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
