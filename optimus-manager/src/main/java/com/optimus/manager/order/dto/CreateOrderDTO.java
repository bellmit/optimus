package com.optimus.manager.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
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
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商户客户端IP[商户下单时传入]
     */
    private String clientIp;

    /**
     * 回调商户地址[商户下单时传入]
     */
    private String merchantCallbackUrl;

    /**
     * 商户重定向地址[商户下单时传入]
     */
    private String redirectUrl;

    /**
     * 网关渠道
     */
    private GatewayChannelDTO gatewayChannel;

    /**
     * 网关子渠道
     */
    private GatewaySubChannelDTO gatewaySubChannel;

}
