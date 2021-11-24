package com.optimus.web.order.resp;

import java.io.Serializable;

import com.optimus.util.constants.order.OrderStatusEnum;

import lombok.Data;

/**
 * 查询订单信息Resp
 * 
 * @author sunxp
 */
@Data
public class QueryOrderInfoResp implements Serializable {

    private static final long serialVersionUID = 3509146466995648790L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

    /**
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

}
