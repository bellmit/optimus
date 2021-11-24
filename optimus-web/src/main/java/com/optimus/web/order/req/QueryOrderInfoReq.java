package com.optimus.web.order.req;

import com.optimus.util.req.Req;

import lombok.Data;

/**
 * 查询订单信息Req
 * 
 * @author sunxp
 */
@Data
public class QueryOrderInfoReq extends Req {

    private static final long serialVersionUID = -6113183905321015429L;

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

}
