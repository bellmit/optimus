package com.optimus.web.collect;

import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.MatchChannelDTO;
import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.util.constants.MemberEnum;
import com.optimus.util.constants.OrderEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.collect.req.*;
import com.optimus.web.collect.resp.*;
import com.optimus.web.collect.validate.CollectValidate;
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

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_A.getCode(), req.getMemberInfo().getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理");
        }

        // 创建订单
        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderType(OrderEnum.ORDER_TYPE_R.getCode());
        createOrder.setOrderAmount(req.getAmount());
        orderService.createOrder(createOrder);

        // 返回信息
        ApplyForRechargeResp resp = new ApplyForRechargeResp();
        resp.setOrderId(createOrder.getOrderId());

        return resp;

    }

    /**
     * 确认充值
     *
     * @param req
     * @return ConfirmForRechargeResp
     */
    @PostMapping("/confirmForRecharge")
    public ConfirmForRechargeResp confirmForRecharge(@RequestBody ConfirmForRechargeReq req) {

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_S.getCode(), req.getMemberInfo().getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为平台");
        }

        // 获取下级会员信息
        // 验证下级会员类型为代理

        // 获取订单信息

        // 支付订单

        return new ConfirmForRechargeResp();
    }

    /**
     * 充值
     *
     * @param req
     * @return RechargeResp
     */
    @PostMapping("/recharge")
    public RechargeResp recharge(@RequestBody RechargeReq req) {
        // 获取会员信息
        MemberInfoDTO memberInfo = req.getMemberInfo();

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_C.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或码商");
        }

        // 获取下级会员信息

        // 验证上下级关系

        // 创建订单

        // 支付订单

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

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_A.getCode(), req.getMemberInfo().getMemberType())
                || !StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_M.getCode(), req.getMemberInfo().getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或管理");
        }

        // 验证账户余额是否充足

        // 创建订单
        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderType(OrderEnum.ORDER_TYPE_W.getCode());
        createOrder.setOrderAmount(req.getAmount());
        orderService.createOrder(createOrder);

        // 返回信息
        ApplyForWithdrawResp resp = new ApplyForWithdrawResp();
        resp.setOrderId(createOrder.getOrderId());

        return resp;
    }

    /**
     * 确认提现
     *
     * @param req
     * @return ConfirmForWithdrawReq
     */
    @PostMapping("/confirmForWithdraw")
    public ConfirmForWithdrawResp confirmForWithdraw(@RequestBody ConfirmForWithdrawReq req) {

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_S.getCode(), req.getMemberInfo().getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为平台");
        }

        // 获取下级会员信息
        // 验证下级会员类型为代理或者管理

        // 获取订单信息

        // 支付订单

        return new ConfirmForWithdrawResp();

    }

    /**
     * 提现
     *
     * @param req
     * @return WithdrawResp
     */
    @PostMapping("/withdraw")
    public WithdrawResp withdraw(@RequestBody WithdrawReq req) {

        // 获取会员信息
        MemberInfoDTO memberInfo = req.getMemberInfo();

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_C.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或码商");
        }

        // 获取下级会员信息

        // 验证上下级关系

        // 验证账户余额是否充足

        // 创建订单

        // 支付订单

        return new WithdrawResp();

    }

    /**
     * 划账
     *
     * @param req
     * @return TransferResp
     */
    @PostMapping("/transfer")
    public TransferResp transfer(@RequestBody TransferReq req) {

        // 获取会员信息
        MemberInfoDTO memberInfo = req.getMemberInfo();

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或管理");
        }

        // 创建订单

        // 支付订单

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
