package com.optimus.service.order.impl;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.core.OrderFactory;
import com.optimus.service.order.dto.ConfirmOrderDTO;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.OrderEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {
        OrderEnum orderType = OrderEnum.valueOfType(createOrder.getOrderType());
        BaseOrder baseOrder = orderFactory.getBaseOrder(orderType);

        if (Objects.isNull(baseOrder)) {
            throw new OptimusException(RespCodeEnum.ORDER_TYPE_ERROR);
        }

        PayOrderDTO payOrder = new PayOrderDTO();
        payOrder.setMemberId(createOrder.getMemberId());
        payOrder.setOrderId("");
        payOrder.setActualAmount(new BigDecimal("0"));
        payOrder.setOrderAmount(new BigDecimal("0"));
        payOrder.setFee(new BigDecimal("0"));
        payOrder.setPayTime(DateUtil.currentDate());
        payOrder.setBehavior("");
        payOrder.setChannelReturnMessage("");

        baseOrder.createOrder(payOrder);

        // 验证上游订单是否重复

        // 根据订单类型处理订单金额

        // 落库

        // 回写参数赋值
        return null;
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

}
