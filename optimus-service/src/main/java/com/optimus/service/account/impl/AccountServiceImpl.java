package com.optimus.service.account.impl;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.repuest.ApplyForRechargeRequest;
import org.springframework.stereotype.Service;

/**
 * 账户服务
 *
 * @author sunxp
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public String applyForRecharge(ApplyForRechargeRequest request) {
        return "R2021102510305599900000001";
    }

}
