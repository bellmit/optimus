package com.optimus.manager.order;

import com.optimus.manager.order.dto.OrderInfoDTO;

/**
 * 订单manager
 *
 * @author hongp
 */
public interface OrderManager {

    /**
     * 更新订单信息为订单失败
     * 
     * 注意:此方法不验证原状态
     * 
     * @param id
     */
    void updateOrderInfoToFail(Long id);

    /**
     * 幂等
     *
     * @param orderInfo
     * @return
     */
    Long idempotent(OrderInfoDTO orderInfo);

    /**
     * 释放订单
     * 
     * @param orderInfo
     * @return
     */
    boolean release(OrderInfoDTO orderInfo);

    /**
     * 分润
     * 
     * @param orderInfo
     * @return
     */
    boolean splitProfit(OrderInfoDTO orderInfo);

    /**
     * 订单通知
     * 
     * @param orderInfo
     * @return
     */
    boolean orderNotice(OrderInfoDTO orderInfo);

    /**
     * 异步释放订单
     * 
     * @param orderInfo
     */
    void asyncRelease(OrderInfoDTO orderInfo);

    /**
     * 异步分润
     * 
     * @param orderInfo
     */
    void asyncSplitProfit(OrderInfoDTO orderInfo);

    /**
     * 异步订单通知
     * 
     * @param orderInfo
     */
    void asyncOrderNotice(OrderInfoDTO orderInfo);

}
