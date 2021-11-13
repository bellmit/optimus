package com.optimus.service.order.core;

import com.optimus.service.order.dto.PayOrderDTO;
import org.springframework.stereotype.Service;

/**
 * 划账
 *
 * @author hongp
 */
@Service
public class TransferOrder extends BaseOrder {

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
