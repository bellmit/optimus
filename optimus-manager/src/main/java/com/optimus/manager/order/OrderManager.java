package com.optimus.manager.order;

import com.optimus.manager.order.dto.OrderNoticeInputDTO;

/**
 * 订单manager
 *
 * @author hongp
 */
public interface OrderManager {

    /**
     * 验证上游单号重复
     *
     * @param callerOrderId
     * @return
     */
    void checkCallerOrderId(String callerOrderId);

    /**
     * 订单通知
     * 
     * @param input
     * @param key
     * @param noticeUrl
     * @return
     */
    String orderNotice(OrderNoticeInputDTO input, String key, String noticeUrl);

}
