package com.optimus.web.collect;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.*;
import com.optimus.web.collect.req.*;
import com.optimus.web.collect.resp.*;
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


    /**
     * 充值
     *
     * @param req
     * @return RechargeResp
     */
    @GetMapping("/recharge")
    public RechargeResp recharge(@RequestBody RechargeReq req) {

        RechargeDTO rechargeDTO = new RechargeDTO();
        BeanUtils.copyProperties(req, rechargeDTO);
        accountService.recharge(rechargeDTO);

        return new RechargeResp();
    }


    /**
     * 申请提现
     *
     * @param req
     * @return ApplyForWithdrawResp
     */
    @GetMapping("/applyForWithdraw")
    public ApplyForWithdrawResp applyForWithdraw(@RequestBody ApplyForWithdrawReq req) {

        ApplyForWithdrawDTO applyForWithdrawDTO = new ApplyForWithdrawDTO();
        BeanUtils.copyProperties(req, applyForWithdrawDTO);

        ApplyForWithdrawResp resp = new ApplyForWithdrawResp();
        resp.setOrderId(accountService.applyForWithdraw(applyForWithdrawDTO));

        return resp;
    }


    /**
     * 提现
     *
     * @param req
     * @return WithdrawResp
     */
    @GetMapping("/withdraw")
    public WithdrawResp withdraw(@RequestBody WithdrawReq req) {

        WithdrawDTO withdrawDTO = new WithdrawDTO();
        BeanUtils.copyProperties(req, withdrawDTO);

        accountService.withdraw(withdrawDTO);

        return new WithdrawResp();
    }


    /**
     * 提现
     *
     * @param req
     * @return TransferResp
     */
    @GetMapping("/transfer")
    public TransferResp transfer(@RequestBody TransferReq req) {

        TransferDTO transferDTO = new TransferDTO();
        BeanUtils.copyProperties(req, transferDTO);

        accountService.transfer(transferDTO);

        return new TransferResp();
    }

}
