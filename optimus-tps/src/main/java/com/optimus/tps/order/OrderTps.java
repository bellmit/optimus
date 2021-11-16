package com.optimus.tps.order;

import com.optimus.tps.order.req.OrderNoticeReq;

/**
 * 订单Tps
 * 
 * @author sunxp
 */
public interface OrderTps {

    /**
     * 订单通知
     * 
     * @param req
     * @param key
     * @param noticeUrl
     * @return
     */
    String orderNotice(OrderNoticeReq req, String key, String noticeUrl);

}
