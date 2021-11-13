package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.optimus.util.constants.OrderEnum;

import lombok.Data;

/**
 * 订单信息DO
 * 
 * @author sunxp
 */
@Data
public class OrderInfoDO implements Serializable {

    private static final long serialVersionUID = 7893087806643477923L;

    /**
     * 主键
     */
    private Long id;

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
     * 被调用方订单编号
     */
    private String calleeOrderId;

    /**
     * 订单类型
     * 
     * @see OrderEnum
     */
    private String orderType;

    /**
     * 订单状态
     * 
     * @see OrderEnum
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
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 分润状态
     * 
     * @see OrderEnum
     */
    private String splitProfitStatus;

    /**
     * 行为
     */
    private String behavior;

    /**
     * 商户回调地址
     */
    private String merchantCallbackUrl;

    /**
     * 商户回调次数
     */
    private Short merchantCallbackCount;

    /**
     * 商户通知状态
     * 
     * @see OrderEnum
     */
    private String merchantNotifyStatus;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 网关子渠道编号
     */
    private String subChannelCode;

    /**
     * 网关渠道返回信息
     */
    private String channelReturnMessage;

    /**
     * 网关渠道订单查询次数
     */
    private Short channelOrderQueryCount;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}