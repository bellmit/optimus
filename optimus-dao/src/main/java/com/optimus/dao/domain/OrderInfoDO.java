package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * OrderInfoDO
 */
@Data
public class OrderInfoDO implements Serializable {

    private static final long serialVersionUID = 7893087806643477923L;

    private Long id;

    private String memberId;

    private String supDirectMemberId;

    private String codeMemberId;

    private String orderId;

    private String callerOrderId;

    private String calleeOrderId;

    private String orderType;

    private String orderStatus;

    private BigDecimal orderAmount;

    private BigDecimal actualAmount;

    private Date orderTime;

    private Date payTime;

    private String splitProfitStatus;

    private String behavior;

    private String merchantCallbackUrl;

    private Short merchantCallbackCount;

    private String merchantNotifyStatus;

    private String channelCode;

    private String subChannelCode;

    private String channelReturnMessage;

    private Short channelOrderQueryCount;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}