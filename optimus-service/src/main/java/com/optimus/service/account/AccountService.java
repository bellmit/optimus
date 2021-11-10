package com.optimus.service.account;

import com.optimus.service.account.dto.ApplyForRechargeDTO;

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

}
