package com.optimus.service.order.impl;

import java.util.List;

import com.optimus.manager.account.dto.ChangeAmountDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.CreateOrderDTO;

import org.springframework.stereotype.Service;

/**
 * OrderServiceImpl
 * 
 * @author sunxp
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(CreateOrderDTO createOrderDTO) {

    }

    @Override
    public List<ChangeAmountDTO> buildSplitProfit(List<InviteChainDTO> inviteChainDTOList) {
        return null;
    }

}
