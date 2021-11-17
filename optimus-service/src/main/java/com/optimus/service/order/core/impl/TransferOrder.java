package com.optimus.service.order.core.impl;

import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.order.convert.OrderServiceConvert;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTransferTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        OrderInfoDTO orderInfo = OrderServiceConvert.getOrderInfoDTO(createOrder);
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

        List<DoTransDTO> doTransList = new ArrayList<>();

        // 预付款户到余额户
        if (StringUtils.pathEquals(OrderTransferTypeEnum.ORDER_TRANSFER_TYPE_A2B.getCode(), payOrder.getTransferType())) {
            // 加一笔余额
            DoTransDTO bTrans = OrderServiceConvert.getDoTransDTO(payOrder.getMemberId(), payOrder.getOrderId(), payOrder.getOrderType());
            bTrans.setChangeType(AccountChangeTypeEnum.B_PLUS.getCode());
            bTrans.setAmount(payOrder.getActualAmount());
            bTrans.setRemark("预付款户到余额户");
            doTransList.add(bTrans);

            // 减一笔预付款
            DoTransDTO aTrans = OrderServiceConvert.getDoTransDTO(payOrder.getMemberId(), payOrder.getOrderId(), payOrder.getOrderType());
            aTrans.setChangeType(AccountChangeTypeEnum.A_MINUS.getCode());
            aTrans.setAmount(payOrder.getActualAmount());
            aTrans.setRemark("预付款户到余额户");
            doTransList.add(aTrans);
        }

        // 余额户到预付款户
        if (StringUtils.pathEquals(OrderTransferTypeEnum.ORDER_TRANSFER_TYPE_B2A.getCode(), payOrder.getTransferType())) {
            // 减一笔余额
            DoTransDTO bMinusTrans = OrderServiceConvert.getDoTransDTO(payOrder.getMemberId(), payOrder.getOrderId(), payOrder.getOrderType());
            bMinusTrans.setChangeType(AccountChangeTypeEnum.B_MINUS.getCode());
            bMinusTrans.setAmount(payOrder.getActualAmount());
            bMinusTrans.setRemark("余额户到预付款户");
            doTransList.add(bMinusTrans);

            // 加一笔预付款
            DoTransDTO aPlusTrans = OrderServiceConvert.getDoTransDTO(payOrder.getMemberId(), payOrder.getOrderId(), payOrder.getOrderType());
            aPlusTrans.setChangeType(AccountChangeTypeEnum.A_PLUS.getCode());
            aPlusTrans.setAmount(payOrder.getActualAmount());
            aPlusTrans.setRemark("余额户到预付款户");
            doTransList.add(aPlusTrans);
        }

        accountManager.doTrans(doTransList);

        // 更新订单状态
        orderInfoDao.updateStatusByOrderId(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AP.getCode());

    }
}
