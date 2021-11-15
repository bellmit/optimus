package com.optimus.service.order.core.factory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.constants.order.OrderTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import groovy.lang.Singleton;

/**
 * 订单处理工厂
 *
 * @author hongp
 */
@Component
@Singleton
public class OrderFactory {

    @Autowired
    public Map<String, BaseOrder> baseOrderMap = new ConcurrentHashMap<>();

    /**
     * 获取订单实例
     * 
     * @param orderType
     * @return
     */
    public BaseOrder getOrderInstance(String orderType) {

        OrderTypeEnum instance = OrderTypeEnum.instanceOf(orderType);
        if (Objects.isNull(instance)) {
            return null;
        }

        BaseOrder baseOrder = baseOrderMap.get(instance.getInstance());
        if (Objects.isNull(baseOrder)) {
            return null;
        }

        return baseOrder;

    }

}
