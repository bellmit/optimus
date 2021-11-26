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
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.order.OrderConfirmTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 充值
 *
 * @author hongp
 */
@Component
public class RechargeOrder extends BaseOrder {

    @Autowired
    private AccountManager accountManager;

    @Resource
    private OrderInfoDao orderInfoDao;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
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

        // 驳回
        if (StringUtils.pathEquals(OrderConfirmTypeEnum.ORDER_CONFIRM_TYPE_R.getCode(), payOrder.getConfirmType())) {
            // 更新订单状态
            orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AC.getCode(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
            return;
        }

        // 更新订单状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AP.getCode(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
        if (update != 1) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR, "订单状态异常");
        }

        // 通过
        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 加一笔余额
        DoTransDTO bPlus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, payOrder, "充值");
        doTransList.add(bPlus);

        // 如果是码商给下级充值,则码商自己需要减一笔余额
        // 若supMemberInfo为空,则作为异常处理
        if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), payOrder.getSupMemberInfo().getMemberType())) {
            // 减一笔余额
            DoTransDTO bMinus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, payOrder, "充值减余额");
            doTransList.add(bMinus);
        }

        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败,回滚订单状态
        if (!doTrans) {
            orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), OrderStatusEnum.ORDER_STATUS_AP.getCode(), DateUtil.currentDate());
        }

    }

}
