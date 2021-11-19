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
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 描述
     */
    private String memo;

    /**
     * 网关渠道返回信息[商户下单后返回商户]
     */
    private String channelReturnMessage;

}
