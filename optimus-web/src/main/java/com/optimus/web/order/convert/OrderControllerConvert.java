package com.optimus.web.order.convert;

import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.web.order.req.QueryOrderInfoReq;
import com.optimus.web.order.resp.QueryOrderInfoResp;

/**
 * 订单ControllerConvert
 * 
 * @author sunxp
 */
public class OrderControllerConvert {

    /**
     * 获取订单信息DTO
     * 
     * @param req
     * @return
     */
    public static OrderInfoDTO getOrderInfoDTO(QueryOrderInfoReq req) {

        // 订单信息DTO
        OrderInfoDTO orderInfo = new OrderInfoDTO();

        orderInfo.setMemberId(req.getMemberId());
        orderInfo.setOrderId(req.getOrderId());
        orderInfo.setCallerOrderId(req.getCallerOrderId());

        return orderInfo;

    }

    /**
     * 获取查询订单Resp
     * 
     * @param orderInfo
     * @return
     */
    public static QueryOrderInfoResp getQueryOrderInfoResp(OrderInfoDTO orderInfo) {

        // 查询订单Resp
        QueryOrderInfoResp resp = new QueryOrderInfoResp();

        resp.setMemberId(orderInfo.getMemberId());
        resp.setOrderId(orderInfo.getOrderId());
        resp.setCallerOrderId(orderInfo.getCallerOrderId());
        resp.setOrderStatus(orderInfo.getOrderStatus());

        return resp;

    }

}
