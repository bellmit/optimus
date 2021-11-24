package com.optimus.service.order.core.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.AccountInfoDTO;
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

/**
 * 提现
 *
 * @author hongp
 */
@Component
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
        AssertUtil.notEmpty(memberTransConfine.getCollectFeeWay(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "手续费收取方式未配置");

        // 验证账户余额是否充足
        AccountInfoDTO accountInfo = accountManager.getAccountInfoByMemberIdAndAccountType(createOrder.getMemberId(), AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
        if (accountInfo.getAmount().compareTo(createOrder.getOrderAmount()) < 0) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
        }

        // 验证提现金额，必须 最大金额>= 提现金额 >= 最小金额
        if (createOrder.getOrderAmount().compareTo(memberTransConfine.getSingleMaxAmount()) > 0 || createOrder.getOrderAmount().compareTo(memberTransConfine.getSingleMinAmount()) < 0) {
            throw new OptimusException(RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "提现金额不在交易限制范围内");
        }

        // 订单信息DTO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder);

        // 计算手续费
        BigDecimal fee = memberManager.getFee(createOrder.getOrderAmount(), memberTransConfine);
        orderInfo.setFee(fee);

        // 手续费收取方式为到账余额:实际金额与订单金额一致
        if (StringUtils.pathEquals(MemberCollectFeeWayEnum.COLLECT_FEE_WAY_A.getCode(), memberTransConfine.getCollectFeeWay())) {
            orderInfo.setActualAmount(orderInfo.getOrderAmount());
        }

        // 手续费收取方式为余额:实际金额=订单金额+手续费
        if (StringUtils.pathEquals(MemberCollectFeeWayEnum.COLLECT_FEE_WAY_B.getCode(), memberTransConfine.getCollectFeeWay())) {
            orderInfo.setActualAmount(orderInfo.getOrderAmount().add(fee));
        }

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 减一笔余额
        DoTransDTO bMinus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, orderInfo, "余额户到冻结户");
        doTransList.add(bMinus);

        // 加一笔冻结
        DoTransDTO fPlus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_PLUS, orderInfo, "余额户到冻结户");
        doTransList.add(fPlus);

        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败,冻结余额异常
        if (!doTrans) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "冻结余额异常");
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
        if (StringUtils.pathEquals(OrderConfirmTypeEnum.ORDER_CONFIRM_TYPE_R.getCode(), payOrder.getConfirmType())) {
            // 回账
            List<DoTransDTO> doTransList = new ArrayList<>();
            // 加一笔余额
            DoTransDTO bPlus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, payOrder, "冻结户到余额户");
            doTransList.add(bPlus);

            // 减一笔冻结
            DoTransDTO fMinus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_MINUS, payOrder, "冻结户到余额户");
            doTransList.add(fMinus);

            boolean doTrans = accountManager.doTrans(doTransList);

            // 账户交易失败
            if (!doTrans) {
                throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "冻结余额异常");
            }
            return;
        }

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 减一笔冻结
        DoTransDTO fMinus = OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_MINUS, payOrder, "冻结户到余额户");
        doTransList.add(fMinus);

        // 如果有手续费,则平台加一笔手续费
        if (Objects.nonNull(payOrder.getFee()) && payOrder.getFee().compareTo(BigDecimal.ZERO) > 0) {
            // 平台加一笔
            DoTransDTO sysTrans = new DoTransDTO();
            sysTrans.setOrderId(payOrder.getOrderId());
            sysTrans.setChangeType(AccountChangeTypeEnum.P_PLUS.getCode());
            sysTrans.setAmount(payOrder.getFee());
            sysTrans.setRemark("提现手续费");
            doTransList.add(sysTrans);
        }

        boolean doTrans = accountManager.doTrans(doTransList);

        // 账户交易失败,回滚订单状态
        if (!doTrans) {
            orderInfoDao.updateOrderStatusByOrderIdAndOrderStatus(payOrder.getOrderId(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), OrderStatusEnum.ORDER_STATUS_AP.getCode(), DateUtil.currentDate());
        }

    }

}
