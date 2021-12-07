package com.optimus.service.order.impl;

import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.core.factory.OrderFactory;
import com.optimus.util.AssertUtil;
import com.optimus.util.GenerateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单ServiceImpl
 *
 * @author sunxp
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderFactory orderFactory;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Resource
    private OrderManager orderManager;

    @Override
    public OrderInfoDTO getOrderInfoByOrderId(String orderId) {

        // 根据订单编号查询订单信息DO
        OrderInfoDO orderInfoDO = orderInfoDao.getOrderInfoByOrderId(orderId);
        if (Objects.isNull(orderInfoDO)) {
            return null;
        }

        // 订单信息DTO
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfoDO, orderInfoDTO);

        return orderInfoDTO;
    }

    @Override
    public OrderInfoDTO getOrderInfoByMemberIdAndCallerOrderId(String memberId, String callerOrderId) {

        // 查询订单信息
        OrderInfoDO orderInfoDO = orderInfoDao.getOrderInfoByMemberIdAndCallerOrderId(memberId, callerOrderId);
        AssertUtil.notEmpty(orderInfoDO, RespCodeEnum.ORDER_NO, null);

        // 订单信息DTO
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfoDO, orderInfo);

        return orderInfo;

    }

    @Override
    public void updateOrderinfoToFail(Long id) {

        // 订单信息
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        orderInfo.setId(id);
        orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());

        // 更新订单信息
        orderInfoDao.updateOrderInfo(OrderManagerConvert.getOrderInfoDO(orderInfo));

    }

    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // 订单编号
        createOrder.setOrderId(GenerateUtil.generate(createOrder.getOrderType()));

        // 订单幂等
        Long id = orderManager.idempotent(OrderManagerConvert.getOrderInfoDTO(createOrder));
        AssertUtil.notEmpty(id, RespCodeEnum.ORDER_ERROR, "订单幂等异常");

        try {

            // 订单工厂实例
            BaseOrder baseOrder = orderFactory.getOrderInstance(createOrder.getOrderType());
            AssertUtil.notEmpty(baseOrder, RespCodeEnum.ORDER_ERROR, "订单类型异常");

            // 工厂处理订单信息
            OrderInfoDTO orderInfo = baseOrder.createOrder(createOrder);
            orderInfo.setId(id);
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NP.getCode());

            // 更新订单信息
            orderInfoDao.updateOrderInfo(OrderManagerConvert.getOrderInfoDO(orderInfo));

            return orderInfo;

        } catch (OptimusException e) {
            log.error("创建订单异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());

            // 更新订单信息为失败
            updateOrderinfoToFail(id);

            throw e;
        } catch (Exception e) {
            log.error("创建订单异常:", e);

            // 更新订单信息为失败
            updateOrderinfoToFail(id);

            throw new OptimusException(RespCodeEnum.FAILE);
        }

    }

    @Override
    public void payOrder(PayOrderDTO payOrder) {

        // 订单工厂实例
        BaseOrder baseOrder = orderFactory.getOrderInstance(payOrder.getOrderType());
        AssertUtil.notEmpty(baseOrder, RespCodeEnum.ORDER_ERROR, "订单类型异常");

        // 工厂处理订单信息
        baseOrder.payOrder(payOrder);

    }

}
