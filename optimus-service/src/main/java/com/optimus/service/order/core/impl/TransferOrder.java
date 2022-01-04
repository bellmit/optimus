package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTransferTypeEnum;
import com.optimus.util.model.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 划账
 *
 * @author hongp
 */
@Component
@Slf4j
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

        // 订单信息DTO
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

        // 记账List
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 余额户到预付款户
        if (StringUtils.pathEquals(OrderTransferTypeEnum.TRANSFER_TYPE_B2A.getCode(), payOrder.getTransferType())) {

            // 验证账户金额是否充足
            super.checkAccountAmount(payOrder.getMemberId(), payOrder.getOrderAmount(), AccountTypeEnum.ACCOUNT_TYPE_B);

            doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, payOrder, "划账减余额户"));
            doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.A_PLUS, payOrder, "划账加预付款户"));
        }

        // 预付款户到余额户
        if (StringUtils.pathEquals(OrderTransferTypeEnum.TRANSFER_TYPE_A2B.getCode(), payOrder.getTransferType())) {

            // 验证账户金额是否充足
            super.checkAccountAmount(payOrder.getMemberId(), payOrder.getOrderAmount(), AccountTypeEnum.ACCOUNT_TYPE_A);

            doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, payOrder, "划账加余额户"));
            doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.A_MINUS, payOrder, "划账减预付款户"));
        }

        // 更新订单状态
        OrderInfoDO orderInfoDO = OrderManagerConvert.getOrderInfoDO(payOrder, OrderStatusEnum.ORDER_STATUS_AP);
        int update = orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(orderInfoDO, OrderStatusEnum.ORDER_STATUS_NP.getCode());
        if (update != 1) {
            return;
        }

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败
        if (!doTrans) {
            log.warn("划账失败记账失败,订单信息:{}", payOrder);
            throw new OptimusException(RespCodeEnum.ORDER_PLACE_ERROR, "划账记账异常");
        }

    }

}
