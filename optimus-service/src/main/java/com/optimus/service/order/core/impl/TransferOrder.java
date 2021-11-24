package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTransferTypeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 划账
 *
 * @author hongp
 */
@Component
public class TransferOrder extends BaseOrder {

    @Autowired
    private AccountManager accountManager;

    @Resource
    private OrderInfoDao orderInfoDao;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // 获取订单信息DTO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder);
        orderInfo.setActualAmount(createOrder.getOrderAmount());

        return orderInfo;

    }

    /**
     * 支付订单
     *
     * @param payOrder
     */
    @Override
    public void payOrder(PayOrderDTO payOrder) {

        // 更新订单状态
        int update = orderInfoDao.updateStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AP.getCode(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
        if (update != 1) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR, "订单状态异常");
        }

        List<DoTransDTO> doTransList = new ArrayList<>();

        // 预付款户到余额户
        if (StringUtils.pathEquals(OrderTransferTypeEnum.ORDER_TRANSFER_TYPE_A2B.getCode(), payOrder.getTransferType())) {
            // 加一笔余额
            DoTransDTO bPlus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, payOrder, "预付款户到余额户");
            doTransList.add(bPlus);

            // 减一笔预付款
            DoTransDTO aMinus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.A_MINUS, payOrder, "预付款户到余额户");
            doTransList.add(aMinus);
        }

        // 余额户到预付款户
        if (StringUtils.pathEquals(OrderTransferTypeEnum.ORDER_TRANSFER_TYPE_B2A.getCode(), payOrder.getTransferType())) {
            // 减一笔余额
            DoTransDTO bMinus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, payOrder, "余额户到预付款户");
            doTransList.add(bMinus);

            // 加一笔预付款
            DoTransDTO aPlus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.A_PLUS, payOrder, "余额户到预付款户");
            doTransList.add(aPlus);
        }

        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败,回滚订单状态
        if (!doTrans) {
            orderInfoDao.updateStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), OrderStatusEnum.ORDER_STATUS_AP.getCode(), DateUtil.currentDate());
        }

    }
}
