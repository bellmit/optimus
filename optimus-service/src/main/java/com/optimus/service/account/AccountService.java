package com.optimus.service.account;

import com.optimus.service.account.dto.*;

/**
 * 账户服务
 *
 * @author sunxp
 */
public interface AccountService {

    /**
     * 申请充值
     *
     * @param applyForRechargeDTO 申请充值DTO
     * @return 订单编号
     */
    String applyForRecharge(ApplyForRechargeDTO applyForRechargeDTO);


    /**
     * 充值
     *
     * @param rechargeDTO 充值DTO
     */
    void recharge(RechargeDTO rechargeDTO);


    /**
     * 申请提现
     *
     * @param applyForWithdrawDTO 申请提现DTO
     * @return 订单编号
     */
    String applyForWithdraw(ApplyForWithdrawDTO applyForWithdrawDTO);


    /**
     * 提现
     *
     * @param withdrawDTO 提现DTO
     */
    void withdraw(WithdrawDTO withdrawDTO);


    /**
     * 划账
     *
     * @param transferDTO 划账DTO
     */
    void transfer(TransferDTO transferDTO);

}
