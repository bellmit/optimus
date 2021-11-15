package com.optimus.manager.order;

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
}
