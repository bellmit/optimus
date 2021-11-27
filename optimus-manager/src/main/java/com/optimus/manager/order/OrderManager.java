package com.optimus.manager.order;

import java.util.List;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.order.dto.OrderInfoDTO;

/**
 * 订单manager
 *
 * @author hongp
 */
public interface OrderManager {

    /**
     * 检查上游单号重复
     *
     * @param callerOrderId
     * @return
     */
    void checkCallerOrderId(String callerOrderId);

    /**
     * 释放订单
     * 
     * @param orderInfo
     */
    void release(OrderInfoDTO orderInfo);

    /**
     * 分润
     * 
     * @param orderInfo
     * @param chainList
     * @param memberChannelList
     */
    void splitProfit(OrderInfoDTO orderInfo, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList);

    /**
     * 订单通知
     * 
     * @param orderInfo
     */
    void orderNotice(OrderInfoDTO orderInfo);

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
     * @param chainList
     * @param memberChannelList
     */
    void asyncSplitProfit(OrderInfoDTO orderInfo, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList);

    /**
     * 异步订单通知
     * 
     * @param orderInfo
     */
    void asyncOrderNotice(OrderInfoDTO orderInfo);

}
