package com.optimus.web.collect;

import javax.servlet.http.HttpServletRequest;

import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.member.MemberService;
import com.optimus.service.order.OrderService;
import com.optimus.util.AssertUtil;
import com.optimus.util.HostUtil;
import com.optimus.util.annotation.OptimusRateLimiter;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;
import com.optimus.util.constants.member.MemberMerchantOrderSwitchEnum;
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.model.exception.OptimusException;
import com.optimus.web.collect.convert.CollectControllerConvert;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.req.ApplyForWithdrawReq;
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
import com.optimus.web.collect.validate.CollectControllerValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收单Controller
 *
 * @author hongp
 */
@RestController
@RequestMapping(value = "/optimus/collect")
public class CollectController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CommonSystemConfigService commonSystemConfigService;

    @Autowired
    private MemberService memberService;

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
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/applyForRecharge")
    public ApplyForRechargeResp applyForRecharge(@RequestBody ApplyForRechargeReq req) {

        // 验证参数
        CollectControllerValidate.validateApplyForRecharge(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 会员类型必须为代理
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "会员类型必须为代理");

        // 创建订单
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req);
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // 响应信息
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
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/confirmForRecharge")
    public ConfirmForRechargeResp confirmForRecharge(@RequestBody ConfirmForRechargeReq req) {

        // 验证参数
        CollectControllerValidate.validateConfirmForRecharge(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 会员类型必须为平台
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "会员类型必须为平台");

        // 订单信息
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(req.getOrderId());

        // 验证:订单/会员编号合法性/订单类型/订单状态
        AssertUtil.notEmpty(orderInfo, RespCodeEnum.ORDER_ERROR, "订单不存在");
        AssertUtil.notEquals(orderInfo.getMemberId(), req.getSubMemberId(), RespCodeEnum.ORDER_ERROR, null);
        AssertUtil.notEquals(OrderTypeEnum.ORDER_TYPE_R.getCode(), orderInfo.getOrderType(), RespCodeEnum.ORDER_ERROR, "订单类型异常");
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_ERROR, "订单状态异常");

        // 支付订单
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setConfirmType(req.getConfirmType());
        payOrder.setSupMemberId(req.getMemberId());
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new ConfirmForRechargeResp();
    }

    /**
     * 充值
     *
     * @param req
     * @return RechargeResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/recharge")
    public RechargeResp recharge(@RequestBody RechargeReq req) {

        // 验证参数
        CollectControllerValidate.validateRecharge(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 验证会员类型必须为代理或码商
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员类型必须为代理或码商");
        }

        // 下级必须为商户或者码商
        MemberInfoDTO subDirectMemberInfo = memberService.getMemberInfoByMemberId(req.getSubDirectMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), subDirectMemberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), subDirectMemberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "下级会员类型必须为商户或码商");
        }

        // 验证上下级关系
        memberService.checkMemberLevel(memberInfo, req.getSubDirectMemberId());

        // 创建订单
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req);
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // 支付订单
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new RechargeResp();
    }

    /**
     * 申请提现
     *
     * @param req
     * @return ApplyForWithdrawResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/applyForWithdraw")
    public ApplyForWithdrawResp applyForWithdraw(@RequestBody ApplyForWithdrawReq req) {

        // 验证参数
        CollectControllerValidate.validateApplyForWithdraw(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 会员类型必须为管理或代理
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员类型必须为管理或代理");
        }

        // 创建订单
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req, memberInfo.getOrganizeId());
        orderService.createOrder(createOrder);

        // 响应信息
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
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/confirmForWithdraw")
    public ConfirmForWithdrawResp confirmForWithdraw(@RequestBody ConfirmForWithdrawReq req) {

        // 验证参数
        CollectControllerValidate.validateConfirmForWithdraw(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 会员类型必须为平台
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "会员类型必须为平台");

        // 订单信息
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(req.getOrderId());

        // 验证:订单/会员编号合法性/订单类型/订单状态
        AssertUtil.notEmpty(orderInfo, RespCodeEnum.ORDER_ERROR, "订单不存在");
        AssertUtil.notEquals(orderInfo.getMemberId(), req.getSubMemberId(), RespCodeEnum.ORDER_ERROR, null);
        AssertUtil.notEquals(OrderTypeEnum.ORDER_TYPE_W.getCode(), orderInfo.getOrderType(), RespCodeEnum.ORDER_ERROR, "订单类型异常");
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_ERROR, "订单状态异常");

        // 支付订单
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setConfirmType(req.getConfirmType());
        payOrder.setSupMemberId(req.getMemberId());
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new ConfirmForWithdrawResp();
    }

    /**
     * 提现
     *
     * @param req
     * @return WithdrawResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/withdraw")
    public WithdrawResp withdraw(@RequestBody WithdrawReq req) {

        // 验证参数
        CollectControllerValidate.validateWithdraw(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 会员类型必须为代理
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "会员类型必须为代理");

        // 下级必须为商户或者码商
        MemberInfoDTO subDirectMemberInfo = memberService.getMemberInfoByMemberId(req.getSubDirectMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), subDirectMemberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), subDirectMemberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "下级会员类型必须为商户或码商");
        }

        // 验证上下级关系
        memberService.checkMemberLevel(memberInfo, req.getSubDirectMemberId());

        // 创建订单
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req, subDirectMemberInfo.getOrganizeId());
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // 支付订单
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new WithdrawResp();
    }

    /**
     * 划账
     *
     * @param req
     * @return TransferResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/transfer")
    public TransferResp transfer(@RequestBody TransferReq req) {

        // 验证参数
        CollectControllerValidate.validateTransfer(req);

        // 验证访问IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // 会员信息
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // 验证会员类型
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员类型必须为管理或代理");
        }

        // 创建订单
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req);
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // 支付订单
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setTransferType(req.getTransferType());
        orderService.payOrder(payOrder);

        return new TransferResp();

    }

    /**
     * 下单
     *
     * @param req
     * @return
     */
    @OptimusRateLimiter(permits = 100D, timeout = 0)
    @PostMapping("/placeOrder")
    public PlaceOrderResp placeOrder(@RequestBody PlaceOrderReq req) {

        // 验证参数
        CollectControllerValidate.validatePlaceOrder(req);

        // 会员信息
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "会员类型必须为商户");

        // 验证会员交易限制
        MemberTransConfineDTO memberTransConfine = memberService.getMemberTransConfineByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberMerchantOrderSwitchEnum.MERCHANT_ORDER_SWITCH_Y.getCode(), memberTransConfine.getMerchantOrderSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "商户下单开关已关闭");

        // 验证主渠道
        GatewayChannelDTO gatewayChannel = gatewayService.getGatewayChannelByChannelCode(req.getChannelCode());
        AssertUtil.notEquals(GatewayChannelStatusEnum.CHANNEL_STATUS_Y.getCode(), gatewayChannel.getChannelStatus(), RespCodeEnum.GATEWAY_CHANNEL_ERROR, "未启用渠道");

        // 匹配子渠道
        MatchChannelDTO matchChannel = gatewayService.matchChannel(memberInfo, gatewayChannel, req.getAmount());

        // 下单
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req, memberInfo.getOrganizeId());
        createOrder.setSupMemberId(memberInfo.getSupDirectMemberId());
        createOrder.setCodeMemberId(matchChannel.getCodeMemberId());
        createOrder.setGatewayChannel(gatewayChannel);
        createOrder.setGatewaySubChannel(matchChannel.getGatewaySubChannel());

        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_PLACE_ERROR, "下单失败");

        return CollectControllerConvert.getPlaceOrderResp(orderInfo);
    }

}
