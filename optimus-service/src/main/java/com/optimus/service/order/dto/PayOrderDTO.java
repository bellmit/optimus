package com.optimus.service.order.dto;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.constants.order.OrderBehaviorEnum;
import com.optimus.util.constants.order.OrderConfirmTypeEnum;
import com.optimus.util.constants.order.OrderTransferTypeEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
     * 网关渠道返回信息
     */
    private String channelReturnMessage;

    /**
     * 上级会员信息
     */
    private MemberInfoDTO superMemberInfo;

    /**
     * 会员信息
     */
    private MemberInfoDTO memberInfo;

    /**
     * 划账类型 type[类型：余额-预付款/预付款-余额]
     *
     * @see OrderTransferTypeEnum
     */
    private String transferType;

    /**
     * 确认类型
     *
     * @see OrderConfirmTypeEnum
     */
    private String orderConfirmType;

}
