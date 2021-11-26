package com.optimus.manager.order;

import java.util.List;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeInputDTO;

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
     * 异步释放订单
     * 
     * @param orderInfo
     */
    void asyncRelease(OrderInfoDTO orderInfo);

    /**
     * 异步分润
     * 
     * @param orderId
     * @param chainList
     * @param memberChannelList
     */
    void asyncSplitProfit(String orderId, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList);

    /**
     * 异步订单通知
     * 
     * @param input
     * @param noticeUrl
     */
    void asyncOrderNotice(OrderNoticeInputDTO input, String noticeUrl);

}
