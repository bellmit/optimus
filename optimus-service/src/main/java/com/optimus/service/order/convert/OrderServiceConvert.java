package com.optimus.service.order.convert;

import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.util.page.Page;

/**
 * 订单Service转换器
 * 
 * @author sunxp
 */
public class OrderServiceConvert {

    /**
     * 获取订单查询对象
     * 
     * @param orderInfo
     * @param page
     * @return
     */
    public static OrderInfoQuery getOrderInfoQuery(OrderInfoDTO orderInfo, Page page) {

        OrderInfoQuery query = new OrderInfoQuery();

        query.setPage(page);
        query.setMemberId(orderInfo.getMemberId());
        query.setOrderId(orderInfo.getOrderId());
        query.setCallerOrderId(orderInfo.getCallerOrderId());

        return query;

    }

}
