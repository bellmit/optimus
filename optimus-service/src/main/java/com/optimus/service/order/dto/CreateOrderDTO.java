package com.optimus.service.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.optimus.util.constants.order.OrderTypeEnum;

import lombok.Data;

/**
 * 创建订单DTO
 * 
 * @author sunxp
 */
@Data
public class CreateOrderDTO implements Serializable {

    private static final long serialVersionUID = 2345231466186931749L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 上级会员编号
     */
    private String supMemberId;

    /**
     * 码商会员编号
     */
    private String codeMemberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

    /**
     * 订单类型
     * 
     * @see OrderTypeEnum
     */
    private String orderType;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 商户回调地址
     */
    private String merchantCallBackUrl;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 网关子渠道编号
     */
    private String subChannelCode;

}