package com.optimus.service.order.impl;

import java.util.List;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.manager.account.model.ChangeAmountModel;
import com.optimus.service.member.model.InviteChainModel;
import com.optimus.service.order.OrderService;

import org.springframework.stereotype.Service;

/**
 * OrderServiceImpl
 * 
 * @author sunxp
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(OrderInfoDO orderInfoDO) {

    }

    @Override
    public List<ChangeAmountModel> buildSplitProfit(List<InviteChainModel> inviteChainModelList) {
        return null;
    }

}
