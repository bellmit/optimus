package com.optimus.web.order.validate;

import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.web.order.req.QueryOrderInfoReq;

/**
 * 订单Controller验证器
 * 
 * @author sunxp
 */
public class OrderValidate {

    /**
     * 验证查询订单信息入参对象
     * 
     * @param req
     */
    public static void validateQueryOrderInfo(QueryOrderInfoReq req) {

        AssertUtil.notEmpty(req, RespCodeEnum.INVALID_PARAM, "入参对象不能为空");
        AssertUtil.notEmpty(req.getMemberId(), RespCodeEnum.INVALID_PARAM, "会员编号不能为空");
        AssertUtil.notEmpty(req.getOrderId(), RespCodeEnum.INVALID_PARAM, "订单号不能为空");
        AssertUtil.notEmpty(req.getCallerOrderId(), RespCodeEnum.INVALID_PARAM, "调用方订单号不能为空");

    }

}
