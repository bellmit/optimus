package com.optimus.service.order.impl;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.core.factory.OrderFactory;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.GenerateUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 订单服务
 *
 * @author sunxp
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderInfoDao orderInfoDao;

    @Resource
    private OrderFactory orderFactory;

    @Override
    public OrderInfoDTO getOrderInfoByOrderId(String orderId) {
        OrderInfoDO orderInfoDO = orderInfoDao.getOrderInfoByOrderId(orderId);

        if (Objects.isNull(orderInfoDO)) {
            return null;
        }

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfoDO, orderInfoDTO);

        return orderInfoDTO;
    }

    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        BaseOrder baseOrder = orderFactory.getOrderInstance(createOrder.getOrderType());

        AssertUtil.notEmpty(baseOrder, RespCodeEnum.ORDER_TYPE_ERROR, null);

        createOrder.setOrderId(GenerateUtil.generate(createOrder.getOrderType()));

        baseOrder.createOrder(createOrder);

        // 验证上游订单是否重复

        // 根据订单类型处理订单金额

        // 落库

        // 回写参数赋值
        return null;
    }

    @Override
    public void payOrder(PayOrderDTO payOrder) {

    }

    @Override
    public List<DoTransDTO> buildSplitProfit(List<InviteChainDTO> inviteChainList) {
        return null;
    }

}
