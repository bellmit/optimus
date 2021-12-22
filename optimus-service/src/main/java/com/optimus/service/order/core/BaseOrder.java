package com.optimus.service.order.core;

import java.math.BigDecimal;

import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.AccountInfoDTO;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单处理父类
 *
 * @author hongp
 */
@Slf4j
public abstract class BaseOrder {

    @Autowired
    private AccountManager accountManager;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    public abstract OrderInfoDTO createOrder(CreateOrderDTO createOrder);

    /**
     * 支付订单
     *
     * @param payOrder
     */
    public abstract void payOrder(PayOrderDTO payOrder);

    /**
     * 验证账户金额
     * 
     * @param memberId
     * @param orderAmount
     * @param accountTypeEnum
     */
    public void checkAccountAmount(String memberId, BigDecimal orderAmount, AccountTypeEnum accountTypeEnum) {

        log.info("验证账户金额,会员编号:{},订单金额:{},账户类型:{}", memberId, orderAmount, accountTypeEnum.getCode());

        // 验证账户金额是否充足
        AccountInfoDTO accountInfo = accountManager.getAccountInfoByMemberIdAndAccountType(memberId, accountTypeEnum.getCode());
        log.info("查询账户金额:{}", accountInfo);

        // 账户金额不足
        if (accountInfo.getAmount().compareTo(orderAmount) < 0) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户金额不足");
        }

    }

}
