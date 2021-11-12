package com.optimus.service.order.impl;

import java.util.List;

import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.service.order.dto.ConfirmOrderDTO;
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
    public void createOrder(CreateOrderDTO createOrder) {

        // 验证上游订单是否重复

        // 根据订单类型处理订单金额

        // 落库

        // 回写参数赋值
    }

    @Override
    public void confirmOrder(ConfirmOrderDTO confirmOrder) {

    }

    @Override
    public void payOrder(PayOrderDTO payOrder) {

    }

    @Override
    public List<DoTransDTO> buildSplitProfit(List<InviteChainDTO> inviteChainList) {
        return null;
    }

}
