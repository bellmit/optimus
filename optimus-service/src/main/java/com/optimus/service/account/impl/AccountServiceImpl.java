package com.optimus.service.account.impl;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.*;
import org.springframework.stereotype.Service;

/**
 * 账户服务
 *
 * @author sunxp
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public void recharge(RechargeDTO rechargeDTO) {
        return;
    }

    @Override
    public String applyForWithdraw(ApplyForWithdrawDTO applyForWithdrawDTO) {
        return "R2021102510305599900000001";
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        return;
    }

    @Override
    public void transfer(TransferDTO transferDTO) {
        return;
    }
}
