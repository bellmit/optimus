package com.optimus.web.collect;

import java.math.BigDecimal;
import java.util.Objects;

import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.AccountInfoDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.MatchChannelDTO;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.req.ApplyForWithdrawReq;
import com.optimus.web.collect.req.BaseCollectReq;
import com.optimus.web.collect.req.ConfirmForRechargeReq;
import com.optimus.web.collect.req.ConfirmForWithdrawReq;
import com.optimus.web.collect.req.PlaceOrderReq;
import com.optimus.web.collect.req.RechargeReq;
import com.optimus.web.collect.req.TransferReq;
import com.optimus.web.collect.req.WithdrawReq;
import com.optimus.web.collect.resp.ApplyForRechargeResp;
import com.optimus.web.collect.resp.ApplyForWithdrawResp;
import com.optimus.web.collect.resp.ConfirmForRechargeResp;
import com.optimus.web.collect.resp.ConfirmForWithdrawResp;
import com.optimus.web.collect.resp.PlaceOrderResp;
import com.optimus.web.collect.resp.RechargeResp;
import com.optimus.web.collect.resp.TransferResp;
import com.optimus.web.collect.resp.WithdrawResp;
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

    @Autowired
    private MemberService memberService;

    @Autowired
    private AccountService accountService;

    /**
     * 申请充值
     *
     * @param req
     * @return ApplyForRechargeResp
     */
    @PostMapping("/applyForRecharge")
    public ApplyForRechargeResp applyForRecharge(@RequestBody ApplyForRechargeReq req) {

        // 获取会员信息
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理");
        }

        // 创建订单
        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_R.getCode());
        createOrder.setOrderAmount(req.getAmount());
        createOrder.setCallerOrderId(req.getCallerOrderId());

        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // 返回信息
        ApplyForRechargeResp resp = new ApplyForRechargeResp();
        resp.setOrderId(orderInfo.getOrderId());

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

        // 获取会员信息
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为平台");
        }

        // 获取下级会员信息
        MemberInfoDTO subMemberInfo = memberService.getMemberInfoByMemberId(req.getSubMemberId());
        if (Objects.isNull(subMemberInfo)) {
            throw new OptimusException(RespCodeEnum.MEMBER_NO, "下级会员不存在");
        }

        // 验证下级会员类型为代理
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), subMemberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "下级会员类型必须为代理");
        }

        // 获取订单信息
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(req.getOrderId());

        if (Objects.isNull(orderInfo)) {
            throw new OptimusException(RespCodeEnum.ORDER_NO);
        }

        // 支付订单
        payOrder(orderInfo);

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
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或码商");
        }

        // 验证上下级关系
        checkMemberLevel(memberInfo, req.getSubDirectMemberId());

        // 创建订单 支付订单
        createAndPayOrder(req, OrderTypeEnum.ORDER_TYPE_R.getCode());

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
        // 获取会员信息
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或管理");
        }

        // 验证账户余额是否充足
        AccountInfoDTO accountInfo = accountService.getAccountInfoByMemberIdAndAccountType(req.getMemberId(),
                AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
        if (accountInfo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
        }

        // 创建订单
        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_W.getCode());
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

        // 获取会员信息
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为平台");
        }

        // 获取订单信息
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(req.getOrderId());

        if (Objects.isNull(orderInfo)) {
            throw new OptimusException(RespCodeEnum.ORDER_NO);
        }

        // 校验订单里面的会员编号
        if (StringUtils.pathEquals(orderInfo.getMemberId(), req.getSubMemberId())) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR);
        }

        // 支付订单
        payOrder(orderInfo);

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
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或码商");
        }

        // 验证上下级关系
        checkMemberLevel(memberInfo, req.getSubDirectMemberId());

        // 验证账户余额是否充足
        AccountInfoDTO accountInfo = accountService.getAccountInfoByMemberIdAndAccountType(req.getMemberId(),
                AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
        if (accountInfo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
        }

        // 创建订单 支付订单
        createAndPayOrder(req, OrderTypeEnum.ORDER_TYPE_W.getCode());

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
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())
                || !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_TYPE_ERROR, "会员类型必须为代理或管理");
        }

        // 创建订单
        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(req.getMemberId());
        createOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_M.getCode());
        createOrder.setOrderAmount(req.getAmount());
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // 支付订单
        payOrder(orderInfo);

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

    /**
     * 支付订单
     *
     * @param orderInfo
     */
    private void payOrder(OrderInfoDTO orderInfo) {
        PayOrderDTO payOrder = new PayOrderDTO();
        payOrder.setMemberId(orderInfo.getMemberId());
        payOrder.setOrderId(orderInfo.getOrderId());
        payOrder.setActualAmount(orderInfo.getActualAmount());
        payOrder.setOrderAmount(orderInfo.getOrderAmount());
        payOrder.setFee(orderInfo.getFee());
        payOrder.setPayTime(DateUtil.currentDate());
        orderService.payOrder(payOrder);
    }

    /**
     * 创建且支付订单
     *
     * @param baseCollectReq
     * @param orderType
     */
    private void createAndPayOrder(BaseCollectReq baseCollectReq, String orderType) {
        CreateOrderDTO createOrder = new CreateOrderDTO();
        createOrder.setMemberId(baseCollectReq.getMemberId());
        createOrder.setOrderType(orderType);
        createOrder.setOrderAmount(baseCollectReq.getAmount());
        createOrder.setCallerOrderId(baseCollectReq.getCallerOrderId());
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);
        payOrder(orderInfo);
    }

    /**
     * 校验上下级
     *
     * @param memberInfo
     * @param subDirectMemberId
     */
    private void checkMemberLevel(MemberInfoDTO memberInfo, String subDirectMemberId) {
        // 获取下级会员信息
        MemberInfoDTO subMemberInfo = memberService.getMemberInfoByMemberId(subDirectMemberId);
        if (Objects.isNull(subMemberInfo)) {
            throw new OptimusException(RespCodeEnum.MEMBER_NO, "下级会员不存在");
        }

        // 验证上下级关系
        if (!StringUtils.pathEquals(memberInfo.getMemberId(), subMemberInfo.getSupDirectMemberId())) {
            throw new OptimusException(RespCodeEnum.MEMBER_LEVEL_ERROR);
        }
    }

}
