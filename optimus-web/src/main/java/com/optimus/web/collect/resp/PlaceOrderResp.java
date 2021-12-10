package com.optimus.web.collect.resp;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.order.OrderStatusEnum;

import lombok.Data;

/**
 * 下单Resp
 * 
 * @author sunxp
 */
@Data
public class PlaceOrderResp implements Serializable {

    private static final long serialVersionUID = -2712649751146995461L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 实际金额
     */
    private BigDecimal actualAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 网关渠道返回信息[商户下单后返回商户]
     * 
     * {"text":"html代码","url":"url地址"}
     */
    private String message;

}
