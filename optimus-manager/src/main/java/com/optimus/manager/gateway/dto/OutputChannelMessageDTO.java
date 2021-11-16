package com.optimus.manager.gateway.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.order.OrderStatusEnum;

import lombok.Data;

/**
 * 渠道消息输出DTO
 * 
 * @author sunxp
 */
@Data
public class OutputChannelMessageDTO implements Serializable {

    private static final long serialVersionUID = -5836828589921241921L;

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
     * 被调用方订单编号
     */
    private String calleeOrderId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 描述
     */
    private String memo;

    /**
     * 调用渠道返回消息类型
     */
    private String type;

    /**
     * 调用渠道返回类型所对应的消息
     */
    private Object message;

}
