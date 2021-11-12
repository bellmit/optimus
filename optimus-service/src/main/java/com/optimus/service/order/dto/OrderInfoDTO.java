package com.optimus.service.order.dto;

import com.optimus.util.constants.OrderEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息
 *
 * @author hongp
 */
@Data
public class OrderInfoDTO implements Serializable {

    private static final long serialVersionUID = 366023563632364578L;

    /**
     * 会员号
     */
    private String memberId;

    /**
     * 上级会员号
     */
    private String supMemberId;

    private String codeMemberId;

    /**
     * 订单编号
     */
    private String orderId;

    private String callerOrderId;

    private String calleeOrderId;

    /**
     * 订单类型
     * @see OrderEnum
     */
    private String orderType;

    /**
     * 订单状态
     * @see OrderEnum
     */
    private String orderStatus;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 实付金额
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

    private String splitProfitStatus;

    private String behavior;

    private String merchantCallbackUrl;

    private Short merchantCallbackCount;

    private String merchantNotifyStatus;

    private String channelCode;

    private String subChannelCode;

    private String channelReturnMessage;

    private Short channelOrderQueryCount;

}
