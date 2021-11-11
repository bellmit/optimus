package com.optimus.web.collect.req;

import java.math.BigDecimal;

import com.optimus.util.req.Req;

import lombok.Data;

/**
 * 下单
 * 
 * @author sunxp
 */
@Data
public class PlaceOrderReq extends Req {

    private static final long serialVersionUID = -2457534615535060237L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 商户回调地址
     */
    private String mechantCallBackUrl;

}
