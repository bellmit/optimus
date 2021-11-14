package com.optimus.web.collect.resp;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.order.OrderStatusEnum;

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
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

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
    private BigDecimal amount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 调用渠道返回信息类型
     */
    private String type;

    /**
     * 调用渠道返回类型所对应的信息
     */
    private Object message;

}
