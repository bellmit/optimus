package com.optimus.service.order.core.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.member.MemberCollectFeeWayEnum;
import com.optimus.util.constants.order.OrderConfirmTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 提现
 *
 * @author hongp
 */
@Component
@Slf4j
public class WithdrawOrder extends BaseOrder {

    @Autowired
    private MemberManager memberManager;

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

        // 验证会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(createOrder.getMemberId());
        AssertUtil.notEmpty(memberTransConfine.getCollectFeeWay(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置手续费收取方式");
        AssertUtil.notEmpty(memberTransConfine.getWithdrawFeeSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置提现手续费开关");
        AssertUtil.notEmpty(memberTransConfine.getSingleMinAmount(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置单笔最小金额");
        AssertUtil.notEmpty(memberTransConfine.getSingleMaxAmount(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置单笔最大金额");

        // 验证提现金额:最小金额<=提现金额<=最大金额
        if (createOrder.getOrderAmount().compareTo(memberTransConfine.getSingleMaxAmount()) > 0 || createOrder.getOrderAmount().compareTo(memberTransConfine.getSingleMinAmount()) < 0) {
            log.warn("提现失败,订单金额:{},最小限制金额:{},最大限制金额:{}", createOrder.getOrderAmount(), memberTransConfine.getSingleMinAmount(), memberTransConfine.getSingleMaxAmount());
            throw new OptimusException(RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "金额不在限制范围内");
        }

        // 订单信息DTO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder);

        // 获取提现手续费
        BigDecimal fee = memberManager.getFeeForWithdraw(createOrder.getOrderAmount(), memberTransConfine);
        orderInfo.setFee(fee);

        // 手续费收取方式为到账余额:实际金额与订单金额一致
        if (StringUtils.pathEquals(MemberCollectFeeWayEnum.COLLECT_FEE_WAY_A.getCode(), memberTransConfine.getCollectFeeWay())) {
            orderInfo.setActualAmount(orderInfo.getOrderAmount());
        }

        // 手续费收取方式为余额:实际金额=订单金额+手续费
        if (StringUtils.pathEquals(MemberCollectFeeWayEnum.COLLECT_FEE_WAY_B.getCode(), memberTransConfine.getCollectFeeWay())) {
            orderInfo.setActualAmount(orderInfo.getOrderAmount().add(fee));
        }

        // 验证账户金额是否充足
        super.checkAccountAmount(createOrder.getMemberId(), orderInfo.getActualAmount(), AccountTypeEnum.ACCOUNT_TYPE_B);

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, orderInfo, "提现减余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_PLUS, orderInfo, "提现加冻结户"));

        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败,冻结余额异常
        if (!doTrans) {
            log.warn("提现失败记账失败,订单信息:{}", orderInfo);
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "提现冻结余额异常");
        }

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
        if (StringUtils.pathEquals(OrderConfirmTypeEnum.CONFIRM_TYPE_R.getCode(), payOrder.getConfirmType())) {

            // 更新订单状态
            int update = orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AC.getCode(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
            if (update != 1) {
                return;
            }

            // 回滚账户信息
            rollbackAccountInfo(payOrder);
            return;
        }

        // 记账List
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_MINUS, payOrder, "提现确认减冻结户"));

        // 如果有手续费,则平台加一笔手续费
        if (Objects.nonNull(payOrder.getFee()) && payOrder.getFee().compareTo(BigDecimal.ZERO) > 0) {
            // 平台收取手续费
            doTransList.add(new DoTransDTO(memberManager.getSystemMemberId(payOrder.getMemberId()), payOrder.getOrderId(), payOrder.getOrderType(), AccountChangeTypeEnum.P_PLUS.getCode(), payOrder.getFee(), "提现手续费"));
        }

        // 更新订单状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_AP.getCode(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
        if (update != 1) {
            return;
        }

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败
        if (!doTrans) {
            log.warn("提现失败记账失败,订单信息:{}", payOrder);
            rollbackAccountInfo(payOrder);
            throw new OptimusException(RespCodeEnum.ORDER_PLACE_ERROR, "提现记账异常");
        }

    }

    /**
     * 回滚账户信息
     * 
     * @param payOrder
     */
    private void rollbackAccountInfo(PayOrderDTO payOrder) {

        // 记账List
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 回滚
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, payOrder, "提现回滚加余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_MINUS, payOrder, "提现回滚减冻结户"));

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            log.warn("提现回滚账户信息失败,订单信息:{}", payOrder);
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "提现回滚账户信息异常");
        }

    }

}
