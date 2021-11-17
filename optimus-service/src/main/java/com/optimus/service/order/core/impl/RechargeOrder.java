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
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.order.OrderConfirmTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        // 驳回
        if (StringUtils.pathEquals(OrderConfirmTypeEnum.ORDER_CONFIRM_TYPE_R.getCode(), payOrder.getOrderConfirmType())) {
            // 更新订单状态
            orderInfoDao.updateStatusByOrderId(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AF.getCode());
        }

        // 通过
        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        // 加一笔余额
        DoTransDTO bPlusTrans = OrderServiceConvert.getDoTransDTO(payOrder.getMemberId(), payOrder.getOrderId(), payOrder.getOrderType());
        bPlusTrans.setChangeType(AccountChangeTypeEnum.B_PLUS.getCode());
        bPlusTrans.setRemark("充值");
        bPlusTrans.setAmount(payOrder.getActualAmount());
        doTransList.add(bPlusTrans);

        // 如果是码商给下级充值,则码商自己需要减一笔余额
        if(Objects.nonNull(payOrder.getSuperMemberInfo()) && StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), payOrder.getSuperMemberInfo().getMemberType())){
            // 减一笔余额
            DoTransDTO bMinusTrans = OrderServiceConvert.getDoTransDTO(payOrder.getMemberId(), payOrder.getOrderId(), payOrder.getOrderType());
            bMinusTrans.setChangeType(AccountChangeTypeEnum.B_MINUS.getCode());
            bMinusTrans.setRemark("充值");
            bMinusTrans.setAmount(payOrder.getActualAmount());
            doTransList.add(bPlusTrans);
        }
        accountManager.doTrans(doTransList);

        // 更新订单状态
        orderInfoDao.updateStatusByOrderId(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AP.getCode());
    }

}
