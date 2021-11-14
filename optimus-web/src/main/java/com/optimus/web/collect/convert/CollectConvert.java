package com.optimus.web.collect.convert;

import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.req.BaseCollectReq;

/**
 * 收单转换器
 * 
 * @author sunxp
 */
public class CollectConvert {

    /**
     * 获取创建订单传输对象
     * 
     * @param req
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(ApplyForRechargeReq req) {

        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        return createOrder;
    }

    /**
     * 获取创建订单传输对象
     * 
     * @param baseCollectReq
     * @param orderType
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(BaseCollectReq baseCollectReq, String orderType) {

        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setMemberId(baseCollectReq.getMemberId());
        createOrder.setOrderType(orderType);
        createOrder.setOrderAmount(baseCollectReq.getAmount());
        createOrder.setCallerOrderId(baseCollectReq.getCallerOrderId());

        return createOrder;
    }

    /**
     * 获取支付订单传输对象
     * 
     * @param orderInfo
     * @return
     */
    public static PayOrderDTO getPayOrderDTO(OrderInfoDTO orderInfo) {

        PayOrderDTO payOrder = new PayOrderDTO();

        payOrder.setMemberId(orderInfo.getMemberId());
        payOrder.setOrderId(orderInfo.getOrderId());
        payOrder.setActualAmount(orderInfo.getActualAmount());
        payOrder.setOrderAmount(orderInfo.getOrderAmount());
        payOrder.setFee(orderInfo.getFee());
        payOrder.setPayTime(DateUtil.currentDate());

        return payOrder;

    }

}
