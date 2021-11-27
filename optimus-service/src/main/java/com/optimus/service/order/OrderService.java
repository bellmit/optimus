package com.optimus.service.order;

import java.util.List;

import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.model.page.Page;

/**
 * 订单Service
 *
 * @author sunxp
 */
public interface OrderService {

    /**
     * 根据订单编号查询订单信息
     *
     *
     * @param orderId
     * @return OrderInfoDTO
     */
    OrderInfoDTO getOrderInfoByOrderId(String orderId);

    /**
     * 根据OrderInfoQuery查询订单信息
     * 
     * @param orderInfo
     * @param page
     * @return
     */
    List<OrderInfoDTO> listOrderInfoByOrderInfoQuerys(OrderInfoDTO orderInfo, Page page);

    /**
     * 创建订单
     *
     * 适用场景-申请充值/充值/申请提现/提现/划账/下单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    OrderInfoDTO createOrder(CreateOrderDTO createOrder);

    /**
     * 支付订单
     *
     * 适用场景-充值/提现/划账/网关渠道回调
     *
     * @param payOrder
     */
    void payOrder(PayOrderDTO payOrder);

}
