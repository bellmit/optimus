package com.optimus.service.order;

import java.util.List;

import com.optimus.manager.account.dto.ChangeAmountDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.dto.CreateOrderDTO;

/**
 * OrderService
 * 
 * @author sunxp
 */
public interface OrderService {

    /**
     * 创建订单
     * 
     * @param createOrderDTO
     */
    void createOrder(CreateOrderDTO createOrderDTO);

    /**
     * 构建分润信息
     * 
     * @param inviteChainDTOList
     * @return
     */
    List<ChangeAmountDTO> buildSplitProfit(List<InviteChainDTO> inviteChainDTOList);

}
