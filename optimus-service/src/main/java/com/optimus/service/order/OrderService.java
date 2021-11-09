package com.optimus.service.order;

import java.util.List;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.manager.account.model.ChangeAmountModel;
import com.optimus.service.member.model.InviteChainModel;

/**
 * OrderService
 * 
 * @author sunxp
 */
public interface OrderService {

    /**
     * 创建订单
     * 
     * @param orderInfoDO
     */
    void createOrder(OrderInfoDO orderInfoDO);

    /**
     * 构建分润信息
     * 
     * @param inviteChainModelList
     * @return
     */
    List<ChangeAmountModel> buildSplitProfit(List<InviteChainModel> inviteChainModelList);

}
