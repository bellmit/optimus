package com.optimus.web.collect;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.*;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.MatchChannelDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.web.collect.req.*;
import com.optimus.web.collect.resp.*;
import com.optimus.web.collect.validate.CollectValidate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private OrderService orderService;

    /**
     * 申请充值
     *
     * @param req
     * @return ApplyForRechargeResp
     */
    @PostMapping("/applyForRecharge")
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
    @PostMapping("/recharge")
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
    @PostMapping("/applyForWithdraw")
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
    @PostMapping("/withdraw")
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
    @PostMapping("/transfer")
    public TransferResp transfer(@RequestBody TransferReq req) {

        TransferDTO transferDTO = new TransferDTO();
        BeanUtils.copyProperties(req, transferDTO);

        accountService.transfer(transferDTO);

        return new TransferResp();

    }

    /**
     * 下单
     * 
     * @param req
     * @return
     */
    @PostMapping("/placeOrder")
    PlaceOrderResp placeOrder(@RequestBody PlaceOrderReq req) {

        // 参数验证
        CollectValidate.validatePlaceOrder(req);

        // 主渠道验证
        // 有效性&类型

        // 撮合渠道
        String subChannelCode = gatewayService.matchChannel(new MatchChannelDTO());
        if (!StringUtils.hasLength(subChannelCode)) {

        }

        // 下单
        orderService.createOrder(new CreateOrderDTO());

        return new PlaceOrderResp();

    }

}
