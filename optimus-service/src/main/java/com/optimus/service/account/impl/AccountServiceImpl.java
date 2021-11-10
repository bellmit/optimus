package com.optimus.service.account.impl;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.ApplyForRechargeDTO;

import org.springframework.stereotype.Service;

/**
 * 账户服务
 *
 * @author sunxp
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public String applyForRecharge(ApplyForRechargeDTO applyForRechargeDTO) {
        return "R2021102510305599900000001";
    }

}
