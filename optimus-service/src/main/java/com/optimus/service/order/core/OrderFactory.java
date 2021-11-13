package com.optimus.service.order.core;

import com.optimus.util.constants.OrderEnum;
import groovy.lang.Singleton;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 订单处理工厂
 *
 * @author hongp
 */
@Singleton
public class OrderFactory {

    @Resource
    private RechargeOrder rechargeOrder;

    @Resource
    private PlaceOrder placeOrder;

    @Resource
    private TransferOrder transferOrder;

    @Resource
    private WithdrawOrder withdrawOrder;

    public BaseOrder getBaseOrder(OrderEnum orderEnum) {
        if (Objects.isNull(orderEnum)) {
            return null;
        }

        switch (orderEnum) {
            case ORDER_TYPE_R:
                return rechargeOrder;
            case ORDER_TYPE_C:
                return placeOrder;
            case ORDER_TYPE_M:
                return transferOrder;
            case ORDER_TYPE_W:
                return withdrawOrder;
            default:
                return null;
        }
    }

}
