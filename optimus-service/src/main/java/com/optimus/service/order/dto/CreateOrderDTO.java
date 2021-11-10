package com.optimus.service.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

/**
 * CreateOrderDTO
 * 
 * @author sunxp
 */
@Data
public class CreateOrderDTO implements Serializable {

    private static final long serialVersionUID = 2345231466186931749L;

    private String memberId;

    private String supDirectMemberId;

    private String codeMemberId;

    private String orderId;

    private String callerOrderId;

    private String calleeOrderId;

    private String orderType;

    private BigDecimal orderAmount;

    private Date orderTime;

    private Date payTime;

    private String channelCode;

    private String subChannelCode;

    private String channelReturnMessage;

}
