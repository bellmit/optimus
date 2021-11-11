package com.optimus.web.collect.resp;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * PlaceOrderResp
 * 
 * @author sunxp
 */
@Data
public class PlaceOrderResp implements Serializable {

    private static final long serialVersionUID = -2712649751146995461L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 类型
     */
    private String type;

    /**
     * 类型所对应的消息
     */
    private Object message;

}
