package com.optimus.web.account;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.repuest.ApplyForRechargeRequest;
import com.optimus.web.account.req.ApplyForRechargeReq;
import com.optimus.web.account.resp.ApplyForRechargeResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账户服务
 *
 * @author hongp
 */
@RestController
@RequestMapping(value = "/optimus/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 申请充值
     *
     * @param req
     * @return ApplyForRechargeResp
     */
    @GetMapping("/applyForRecharge")
    public ApplyForRechargeResp applyForRecharge(@RequestBody ApplyForRechargeReq req) {
        ApplyForRechargeRequest request = new ApplyForRechargeRequest();
        BeanUtils.copyProperties(req, request);
        ApplyForRechargeResp resp = new ApplyForRechargeResp();
        resp.setOrderId(accountService.applyForRecharge(request));
        return resp;
    }
}
