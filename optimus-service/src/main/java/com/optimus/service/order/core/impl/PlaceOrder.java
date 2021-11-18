package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.mapper.OrderInfoDao;
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
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

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

        log.info("createOrder is {}", createOrder);

        // 获取OrderInfoDTO
        OrderInfoDTO orderInfo = OrderServiceConvert.getOrderInfoDTO(createOrder);

        // 执行脚本
        ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();

        ExecuteScriptOutputDTO output = gatewayManager.executeScript(input);
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), output.getOrderStatus())) {

            // 下单成功将订单状态致为订单失败
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;

        }

        // 下单成功将订单状态致为等待支付
        orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NP.getCode());

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
