package com.optimus.tps.gateway.resp;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.order.OrderStatusEnum;

import lombok.Data;

/**
 * 执行脚本Resp
 * 
 * @author sunxp
 */
@Data
public class ExecuteScriptResp implements Serializable {

    private static final long serialVersionUID = 5196941168862327683L;

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
