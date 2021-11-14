package com.optimus.web.collect.validate;

import java.math.BigDecimal;

import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.collect.req.PlaceOrderReq;

/**
 * 收单Validate
 * 
 * @author sunxp
 */
public class CollectValidate {

    /**
     * 验证商户下单
     * 
     * @param placeOrderReq
     */
    public static void validatePlaceOrder(PlaceOrderReq placeOrderReq) {

        AssertUtil.notEmpty(placeOrderReq, RespCodeEnum.INVALID_PARAM, "入参对象不能为空");
        AssertUtil.notEmpty(placeOrderReq.getMemberId(), RespCodeEnum.INVALID_PARAM, "会员编号不能为空");
        AssertUtil.notEmpty(placeOrderReq.getCallerOrderId(), RespCodeEnum.INVALID_PARAM, "调用方订单号不能为空");
        AssertUtil.notEmpty(placeOrderReq.getOrderAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        AssertUtil.notEmpty(placeOrderReq.getChannelCode(), RespCodeEnum.INVALID_PARAM, "网关渠道编号不能为空");
        AssertUtil.notEmpty(placeOrderReq.getMerchantCallBackUrl(), RespCodeEnum.INVALID_PARAM, "商户回调地址不能为空");

        if (placeOrderReq.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单金额不合法");
        }

    }
}
