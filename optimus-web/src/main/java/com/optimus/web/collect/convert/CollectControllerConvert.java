package com.optimus.web.collect.convert;

import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.req.ApplyForWithdrawReq;
import com.optimus.web.collect.req.PlaceOrderReq;
import com.optimus.web.collect.req.RechargeReq;
import com.optimus.web.collect.req.TransferReq;
import com.optimus.web.collect.req.WithdrawReq;
import com.optimus.web.collect.resp.PlaceOrderResp;

/**
 * 收单ControllerConvert
 * 
 * @author sunxp
 */
public class CollectControllerConvert {

    /**
     * 获取创建订单DTO
     * 
     * @param req
     * @param organizeId
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(PlaceOrderReq req, Long organizeId) {

        // 创建订单DTO
        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrganizeId(organizeId);
        createOrder.setCallerOrderId(req.getCallerOrderId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setOrderTime(DateUtil.currentDate());
        createOrder.setMerchantCallbackUrl(req.getMerchantCallbackUrl());
        createOrder.setClientIp(req.getClientIp());
        createOrder.setRedirectUrl(req.getRedirectUrl());
        createOrder.setRemark(req.getRemark());

        return createOrder;

    }

    /**
     * 获取创建订单DTO
     * 
     * @param req
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(ApplyForRechargeReq req) {

        // 创建订单DTO
        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_R.getCode());
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        return createOrder;

    }

    /**
     * 获取创建订单DTO
     *
     * @param req
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(RechargeReq req) {

        // 创建订单DTO
        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_R.getCode());
        createOrder.setMemberId(req.getSubDirectMemberId());
        createOrder.setSupMemberId(req.getMemberId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        return createOrder;

    }

    /**
     * 获取创建订单DTO
     * 
     * @param req
     * @param organizeId
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(ApplyForWithdrawReq req, Long organizeId) {

        // 创建订单DTO
        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_W.getCode());
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrganizeId(organizeId);
        createOrder.setCallerOrderId(req.getCallerOrderId());
        createOrder.setOrderAmount(req.getAmount());

        return createOrder;

    }

    /**
     * 获取创建订单DTO
     * 
     * @param req
     * @param organizeId
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(WithdrawReq req, Long organizeId) {

        // 创建订单DTO
        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_W.getCode());
        createOrder.setMemberId(req.getSubDirectMemberId());
        createOrder.setSupMemberId(req.getMemberId());
        createOrder.setOrganizeId(organizeId);
        createOrder.setCallerOrderId(req.getCallerOrderId());
        createOrder.setOrderAmount(req.getAmount());

        return createOrder;

    }

    /**
     * 获取创建订单DTO
     * 
     * @param req
     * @return
     */
    public static CreateOrderDTO getCreateOrderDTO(TransferReq req) {

        // 创建订单DTO
        CreateOrderDTO createOrder = new CreateOrderDTO();

        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_M.getCode());
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        return createOrder;

    }

    /**
     * 获取支付订单DTO
     * 
     * @param orderInfo
     * @return
     */
    public static PayOrderDTO getPayOrderDTO(OrderInfoDTO orderInfo) {

        // 支付订单DTO
        PayOrderDTO payOrder = new PayOrderDTO();

        payOrder.setId(orderInfo.getId());
        payOrder.setMemberId(orderInfo.getMemberId());
        payOrder.setSupMemberId(orderInfo.getSupMemberId());
        payOrder.setOrderId(orderInfo.getOrderId());
        payOrder.setActualAmount(orderInfo.getActualAmount());
        payOrder.setOrderAmount(orderInfo.getOrderAmount());
        payOrder.setFee(orderInfo.getFee());
        payOrder.setPayTime(DateUtil.currentDate());
        payOrder.setOrderType(orderInfo.getOrderType());

        return payOrder;

    }

    /**
     * 获取下单Resp
     * 
     * @param orderInfo
     * @return
     */
    public static PlaceOrderResp getPlaceOrderResp(OrderInfoDTO orderInfo) {

        // 下单Resp
        PlaceOrderResp resp = new PlaceOrderResp();

        resp.setMemberId(orderInfo.getMemberId());
        resp.setOrderStatus(orderInfo.getOrderStatus());
        resp.setOrderId(orderInfo.getOrderId());
        resp.setCallerOrderId(orderInfo.getCallerOrderId());
        resp.setAmount(orderInfo.getOrderAmount());
        resp.setActualAmount(orderInfo.getActualAmount());
        resp.setRemark(orderInfo.getRemark());
        resp.setMessage(orderInfo.getOutput().getContent());

        return resp;

    }

}