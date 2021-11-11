package com.optimus.service.order.impl;

import java.util.List;

import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.service.order.dto.CreateOrderDTO;

import org.springframework.stereotype.Service;

/**
 * 订单服务
 * 
 * @author sunxp
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(CreateOrderDTO createOrderDTO) {

    }

    @Override
    public void payOrder(PayOrderDTO payOrderDTO) {

    }

    @Override
    public List<DoTransDTO> buildSplitProfit(List<InviteChainDTO> inviteChainDTOList) {
        return null;
    }

}
