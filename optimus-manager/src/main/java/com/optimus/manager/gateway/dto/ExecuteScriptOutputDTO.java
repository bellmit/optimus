package com.optimus.manager.gateway.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.order.OrderStatusEnum;

import lombok.Data;

/**
 * 网关脚本输出DTO
 * 
 * @author sunxp
 */
@Data
public class ExecuteScriptOutputDTO implements Serializable {

    private static final long serialVersionUID = -5836828589921241921L;

    /**
     * 码商会员编号[自研渠道必须返回]
     */
    private String codeMemberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 被调用方订单编号
     */
    private String calleeOrderId;

    /**
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 网关渠道返回信息[商户下单后渠道返回报文]
     */
    private String channelReturnMessage;

    /**
     * 内容
     * 
     * 作用一:商户下单后返回商户({"text":"html代码","url":"url地址"})
     * 作用二:渠道回调时返回渠道(不同渠道的成功标识)
     */
    private String content;

}