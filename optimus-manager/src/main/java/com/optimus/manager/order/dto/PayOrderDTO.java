package com.optimus.manager.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.util.constants.order.OrderBehaviorEnum;
import com.optimus.util.constants.order.OrderConfirmTypeEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTransferTypeEnum;
import com.optimus.util.constants.order.OrderTypeEnum;

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
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

    /**
     * 释放状态
     * 
     * @see OrderReleaseStatusEnum
     */
    private String releaseStatus;

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
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 行为
     *
     * @see OrderBehaviorEnum
     */
    private String behavior;

    /**
     * 回调商户地址[商户下单时传入]
     */
    private String merchantCallbackUrl;

    /**
     * 划账类型
     *
     * @see OrderTransferTypeEnum
     */
    private String transferType;

    /**
     * 确认类型
     *
     * @see OrderConfirmTypeEnum
     */
    private String confirmType;

    /**
     * 上级会员信息
     */
    private MemberInfoDTO supMemberInfo;

}
