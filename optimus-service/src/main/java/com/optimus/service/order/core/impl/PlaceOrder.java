package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.service.order.convert.OrderServiceConvert;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.constants.order.OrderStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 下单
 *
 * @author hongp
 */
@Component
@Slf4j
public class PlaceOrder extends BaseOrder {

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private GatewayManager gatewayManager;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        log.info("createOrder is {}", createOrder);

        // 获取OrderInfoDTO
        OrderInfoDTO orderInfo = OrderServiceConvert.getOrderInfoDTO(createOrder);

        // 执行脚本
        ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();

        ExecuteScriptOutputDTO output = gatewayManager.executeScript(input);
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), output.getOrderStatus())) {

            // 构建订单DTO

        }

        // 判断是否需要冻结码商余额

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        accountManager.doTrans(doTransList);

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
