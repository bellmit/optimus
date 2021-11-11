package com.optimus.service.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 支付订单DTO
 * 
 * @author sunxp
 */
@Data
public class PayOrderDTO implements Serializable {

    private static final long serialVersionUID = -7473515200152564812L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 行为
     */
    private String behavior;

    /**
     * 网关渠道返回消息
     */
    private String channelReturnMessage;

}
