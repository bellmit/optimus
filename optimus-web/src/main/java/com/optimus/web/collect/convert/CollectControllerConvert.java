package com.optimus.web.collect.convert;

import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.req.ApplyForWithdrawReq;
import com.optimus.web.collect.req.PlaceOrderReq;
import com.optimus.web.collect.req.RechargeReq;
import com.optimus.web.collect.req.TransferReq;
import com.optimus.web.collect.req.WithdrawReq;
import com.optimus.web.collect.resp.PlaceOrderResp;

/**
 * 收单Controller转换器
 * 
 * @author sunxp
 */
public class CollectControllerConvert {

    /**
     * 获取网关渠道传输对象
     * 
     * @param req
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(PlaceOrderReq req) {

        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setCallerOrderId(req.getCallerOrderId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setMerchantCallBackUrl(req.getMerchantCallBackUrl());
        createOrder.setClientIp(req.getClientIp());
        createOrder.setRedirectUrl(req.getRedirectUrl());

        return createOrder;

    }

    /**
     * 获取创建订单传输对象
     * 
     * @param req
     *            ApplyForRechargeReq
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
     * @param req
     *            ApplyForWithdrawReq
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(ApplyForWithdrawReq req) {

        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderAmount(req.getAmount());

        return createOrder;

    }

    /**
     * 获取创建订单传输对象
     * 
     * @param req
     *            WithdrawReq
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(WithdrawReq req) {

        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setMemberId(req.getSubDirectMemberId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        return createOrder;

    }

    /**
     * 获取创建订单传输对象
     *
     * @param req
     *            RechargeReq
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(RechargeReq req) {

        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setMemberId(req.getSubDirectMemberId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        return createOrder;

    }

    /**
     * 获取创建订单传输对象
     * 
     * @param req
     *            TransferReq
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(TransferReq req) {

        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderAmount(req.getAmount());

        return createOrder;

    }

    /**
     * 获取支付订单传输对象
     * 
     * @param orderInfo
     *            OrderInfoDTO
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
        payOrder.setOrderType(orderInfo.getOrderType());

        return payOrder;

    }

    /**
     * 获取下单响应对象
     * 
     * @param orderInfo
     * @return
     */
    public static PlaceOrderResp getPlaceOrderResp(OrderInfoDTO orderInfo) {

        PlaceOrderResp resp = new PlaceOrderResp();
        resp.setMemberId(orderInfo.getMemberId());
        resp.setOrderStatus(orderInfo.getOrderStatus());
        resp.setOrderId(orderInfo.getOrderId());
        resp.setCallerOrderId(orderInfo.getCallerOrderId());
        resp.setAmount(orderInfo.getOrderAmount());
        resp.setActualAmount(orderInfo.getActualAmount());
        resp.setChannelReturnMessage(orderInfo.getChannelReturnMessage());

        return resp;

    }

}
