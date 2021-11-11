package com.optimus.web.collect.validate;

import java.math.BigDecimal;
import java.util.Objects;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.collect.req.PlaceOrderReq;

import org.springframework.util.StringUtils;

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

        if (Objects.isNull(placeOrderReq)) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "入参对象不能为空");
        }

        if (!StringUtils.hasLength(placeOrderReq.getMemberId())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "会员编号不能为空");
        }

        if (!StringUtils.hasLength(placeOrderReq.getCallerOrderId())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "调用方订单号不能为空");
        }

        if (null == placeOrderReq.getOrderAmount()) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        }

        if (placeOrderReq.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单金额不合法");
        }

        if (!StringUtils.hasLength(placeOrderReq.getChannelCode())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "网关渠道编号不能为空");
        }

        if (!StringUtils.hasLength(placeOrderReq.getMechantCallBackUrl())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "商户回调地址不能为空");
        }

    }
}
