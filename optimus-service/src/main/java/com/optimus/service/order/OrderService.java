package com.optimus.service.order;

import java.util.List;

import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.service.order.dto.ConfirmOrderDTO;
import com.optimus.service.order.dto.CreateOrderDTO;

/**
 * 订单服务
 * 
 * @author sunxp
 */
public interface OrderService {

    /**
     * 创建订单
     * 
     * 适用场景-申请充值/充值/申请提现/提现/划账/下单
     * 
     * @param createOrderDTO
     */
    void createOrder(CreateOrderDTO createOrderDTO);

    /**
     * 调用网关渠道下单返回
     * 
     * 适用场景-调用网关渠道下单返回
     * 
     * @param confirmOrderDTO
     */
    void confirmOrder(ConfirmOrderDTO confirmOrderDTO);

    /**
     * 支付订单
     * 
     * 适用场景-充值/提现/划账/网关渠道回调
     * 
     * @param payOrderDTO
     */
    void payOrder(PayOrderDTO payOrderDTO);

    /**
     * 构建交易信息
     * 
     * 适用场景-网关渠道回调成功
     * 
     * @param inviteChainDTOList
     * @return
     */
    List<DoTransDTO> buildSplitProfit(List<InviteChainDTO> inviteChainDTOList);

}
