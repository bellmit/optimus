package com.optimus.service.order.core.impl;

import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.AccountInfoDTO;
import com.optimus.service.order.convert.OrderServiceConvert;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.member.MemberCollectFeeWayEnum;
import com.optimus.util.exception.OptimusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 提现
 *
 * @author hongp
 */
@Component
public class WithdrawOrder extends BaseOrder {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MemberManager memberManager;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // 验证账户余额是否充足
        AccountInfoDTO accountInfo = accountService.getAccountInfoByMemberIdAndAccountType(createOrder.getMemberId(),
                AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
        if (accountInfo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
        }

        OrderInfoDTO orderInfo = OrderServiceConvert.getOrderInfoDTO(createOrder);

        // 验证会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(createOrder.getMemberId());
        BigDecimal fee = memberManager.getFee(createOrder.getOrderAmount(), memberTransConfine);
        orderInfo.setFee(fee);

        AssertUtil.notEmpty(memberTransConfine.getCollectFeeWay(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "手续费收取方式未配置");

        if (StringUtils.pathEquals(MemberCollectFeeWayEnum.COLLECT_FEE_WAY_A.getCode(), memberTransConfine.getCollectFeeWay())) {
            orderInfo.setActualAmount(orderInfo.getOrderAmount());
        }

        if (StringUtils.pathEquals(MemberCollectFeeWayEnum.COLLECT_FEE_WAY_B.getCode(), memberTransConfine.getCollectFeeWay())) {
            orderInfo.setActualAmount(orderInfo.getOrderAmount().add(fee));
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

        //

    }
}
