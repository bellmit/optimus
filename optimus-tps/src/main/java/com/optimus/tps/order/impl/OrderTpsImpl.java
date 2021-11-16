package com.optimus.tps.order.impl;

import com.optimus.tps.order.OrderTps;
import com.optimus.tps.order.req.OrderNoticeReq;

import org.springframework.stereotype.Component;

/**
 * 订单Tps实现
 * 
 * @author sunxp
 */
@Component
public class OrderTpsImpl implements OrderTps {

    @Override
    public String orderNotice(OrderNoticeReq req, String noticeUrl) {
        return null;
    }

}
