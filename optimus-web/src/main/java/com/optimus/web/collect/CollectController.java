package com.optimus.web.collect;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.ApplyForRechargeDTO;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.resp.ApplyForRechargeResp;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收单web
 * 
 * @author hongp
 */
@RestController
@RequestMapping(value = "/optimus/collect")
public class CollectController {

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

        ApplyForRechargeDTO applyForRechargeDTO = new ApplyForRechargeDTO();
        BeanUtils.copyProperties(req, applyForRechargeDTO);

        ApplyForRechargeResp resp = new ApplyForRechargeResp();
        resp.setOrderId(accountService.applyForRecharge(applyForRechargeDTO));

        return resp;

    }

}
