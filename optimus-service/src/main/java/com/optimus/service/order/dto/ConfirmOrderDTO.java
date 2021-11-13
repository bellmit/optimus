package com.optimus.service.order.dto;

import java.io.Serializable;

import com.optimus.util.constants.OrderEnum;
import lombok.Data;

/**
 * 确认订单DTO
 * 
 * @author sunxp
 */
@Data
public class ConfirmOrderDTO implements Serializable {

    private static final long serialVersionUID = -7473515200152564812L;

    /**
     * 码商会员编号
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
     * @see OrderEnum
     */
    private String orderStatus;

    /**
     * 网关子渠道编号
     */
    private String subChannelCode;

    /**
     * 网关渠道返回信息
     */
    private String channelReturnMessage;

}
