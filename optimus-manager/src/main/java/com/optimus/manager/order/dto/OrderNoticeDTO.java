package com.optimus.manager.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.order.OrderStatusEnum;

import lombok.Data;

/**
 * 订单通知DTO
 * 
 * @author sunxp
 */
@Data
public class OrderNoticeDTO implements Serializable {

    private static final long serialVersionUID = 3304557384990623101L;

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
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 签名
     */
    private String sign;

}
