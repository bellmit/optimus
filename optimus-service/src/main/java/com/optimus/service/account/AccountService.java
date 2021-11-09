package com.optimus.service.account;

import com.optimus.service.account.repuest.ApplyForRechargeRequest;

/**
 * 账户服务
 * 
 * @author sunxp
 */
public interface AccountService {

    /**
     * 申请充值
     *
     * @param request 请求信息
     * @return 订单编号
     */
    String applyForRecharge(ApplyForRechargeRequest request);

}
