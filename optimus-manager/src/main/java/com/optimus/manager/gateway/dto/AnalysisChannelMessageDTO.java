package com.optimus.manager.gateway.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 解析渠道消息
 * 
 * @author sunxp
 */
@Data
public class AnalysisChannelMessageDTO implements Serializable {

    private static final long serialVersionUID = -5836828589921241921L;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回描述
     */
    private String memo;

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
     * 消息
     */
    private String message;

}
