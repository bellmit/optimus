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
 * ??????Controller
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
     * ????????????
     *
     * @param req
     * @return ApplyForRechargeResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/applyForRecharge")
    public ApplyForRechargeResp applyForRecharge(@RequestBody ApplyForRechargeReq req) {

        // ????????????
        CollectControllerValidate.validateApplyForRecharge(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ???????????????????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "???????????????????????????");

        // ????????????
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req);
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // ????????????
        ApplyForRechargeResp resp = new ApplyForRechargeResp();
        resp.setOrderId(orderInfo.getOrderId());

        return resp;

    }

    /**
     * ????????????
     *
     * @param req
     * @return ConfirmForRechargeResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/confirmForRecharge")
    public ConfirmForRechargeResp confirmForRecharge(@RequestBody ConfirmForRechargeReq req) {

        // ????????????
        CollectControllerValidate.validateConfirmForRecharge(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ???????????????????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "???????????????????????????");

        // ????????????
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(req.getOrderId());

        // ??????:??????/?????????????????????/????????????/????????????
        AssertUtil.notEmpty(orderInfo, RespCodeEnum.ORDER_ERROR, "???????????????");
        AssertUtil.notEquals(orderInfo.getMemberId(), req.getSubMemberId(), RespCodeEnum.ORDER_ERROR, null);
        AssertUtil.notEquals(OrderTypeEnum.ORDER_TYPE_R.getCode(), orderInfo.getOrderType(), RespCodeEnum.ORDER_ERROR, "??????????????????");
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_ERROR, "??????????????????");

        // ????????????
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setConfirmType(req.getConfirmType());
        payOrder.setSupMemberId(req.getMemberId());
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new ConfirmForRechargeResp();
    }

    /**
     * ??????
     *
     * @param req
     * @return RechargeResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/recharge")
    public RechargeResp recharge(@RequestBody RechargeReq req) {

        // ????????????
        CollectControllerValidate.validateRecharge(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ??????????????????????????????????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "????????????????????????????????????");
        }

        // ?????????????????????????????????
        MemberInfoDTO subDirectMemberInfo = memberService.getMemberInfoByMemberId(req.getSubDirectMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), subDirectMemberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), subDirectMemberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "??????????????????????????????????????????");
        }

        // ?????????????????????
        memberService.checkMemberLevel(memberInfo, req.getSubDirectMemberId());

        // ????????????
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req);
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // ????????????
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new RechargeResp();
    }

    /**
     * ????????????
     *
     * @param req
     * @return ApplyForWithdrawResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/applyForWithdraw")
    public ApplyForWithdrawResp applyForWithdraw(@RequestBody ApplyForWithdrawReq req) {

        // ????????????
        CollectControllerValidate.validateApplyForWithdraw(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ????????????????????????????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "????????????????????????????????????");
        }

        // ????????????
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req, memberInfo.getOrganizeId());
        orderService.createOrder(createOrder);

        // ????????????
        ApplyForWithdrawResp resp = new ApplyForWithdrawResp();
        resp.setOrderId(createOrder.getOrderId());

        return resp;
    }

    /**
     * ????????????
     *
     * @param req
     * @return ConfirmForWithdrawReq
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/confirmForWithdraw")
    public ConfirmForWithdrawResp confirmForWithdraw(@RequestBody ConfirmForWithdrawReq req) {

        // ????????????
        CollectControllerValidate.validateConfirmForWithdraw(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ???????????????????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "???????????????????????????");

        // ????????????
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(req.getOrderId());

        // ??????:??????/?????????????????????/????????????/????????????
        AssertUtil.notEmpty(orderInfo, RespCodeEnum.ORDER_ERROR, "???????????????");
        AssertUtil.notEquals(orderInfo.getMemberId(), req.getSubMemberId(), RespCodeEnum.ORDER_ERROR, null);
        AssertUtil.notEquals(OrderTypeEnum.ORDER_TYPE_W.getCode(), orderInfo.getOrderType(), RespCodeEnum.ORDER_ERROR, "??????????????????");
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_ERROR, "??????????????????");

        // ????????????
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setConfirmType(req.getConfirmType());
        payOrder.setSupMemberId(req.getMemberId());
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new ConfirmForWithdrawResp();
    }

    /**
     * ??????
     *
     * @param req
     * @return WithdrawResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/withdraw")
    public WithdrawResp withdraw(@RequestBody WithdrawReq req) {

        // ????????????
        CollectControllerValidate.validateWithdraw(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ???????????????????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "???????????????????????????");

        // ?????????????????????????????????
        MemberInfoDTO subDirectMemberInfo = memberService.getMemberInfoByMemberId(req.getSubDirectMemberId());
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), subDirectMemberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), subDirectMemberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "??????????????????????????????????????????");
        }

        // ?????????????????????
        memberService.checkMemberLevel(memberInfo, req.getSubDirectMemberId());

        // ????????????
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req, subDirectMemberInfo.getOrganizeId());
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // ????????????
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setSupMemberInfo(memberInfo);
        orderService.payOrder(payOrder);

        return new WithdrawResp();
    }

    /**
     * ??????
     *
     * @param req
     * @return TransferResp
     */
    @OptimusRateLimiter(permits = 10D, timeout = 0)
    @PostMapping("/transfer")
    public TransferResp transfer(@RequestBody TransferReq req) {

        // ????????????
        CollectControllerValidate.validateTransfer(req);

        // ????????????IP
        String value = commonSystemConfigService.getCommonSystemConfigForCache(CommonSystemConfigBaseKeyEnum.BASE_ACCESS_IP.getCode());
        CollectControllerValidate.validateAccessIp(value, HostUtil.getRemoteIp(httpServletRequest));

        // ????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());

        // ??????????????????
        if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_M.getCode(), memberInfo.getMemberType()) && !StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), memberInfo.getMemberType())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "????????????????????????????????????");
        }

        // ????????????
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req);
        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);

        // ????????????
        PayOrderDTO payOrder = CollectControllerConvert.getPayOrderDTO(orderInfo);
        payOrder.setTransferType(req.getTransferType());
        orderService.payOrder(payOrder);

        return new TransferResp();

    }

    /**
     * ??????
     *
     * @param req
     * @return
     */
    @OptimusRateLimiter(permits = 100D, timeout = 0)
    @PostMapping("/placeOrder")
    public PlaceOrderResp placeOrder(@RequestBody PlaceOrderReq req) {

        // ????????????
        CollectControllerValidate.validatePlaceOrder(req);

        // ????????????
        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), memberInfo.getMemberType(), RespCodeEnum.MEMBER_ERROR, "???????????????????????????");

        // ????????????????????????
        MemberTransConfineDTO memberTransConfine = memberService.getMemberTransConfineByMemberId(req.getMemberId());
        AssertUtil.notEquals(MemberMerchantOrderSwitchEnum.MERCHANT_ORDER_SWITCH_Y.getCode(), memberTransConfine.getMerchantOrderSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "???????????????????????????");

        // ???????????????
        GatewayChannelDTO gatewayChannel = gatewayService.getGatewayChannelByChannelCode(req.getChannelCode());
        AssertUtil.notEquals(GatewayChannelStatusEnum.CHANNEL_STATUS_Y.getCode(), gatewayChannel.getChannelStatus(), RespCodeEnum.GATEWAY_CHANNEL_ERROR, "???????????????");

        // ???????????????
        MatchChannelDTO matchChannel = gatewayService.matchChannel(memberInfo, gatewayChannel, req.getAmount());

        // ??????
        CreateOrderDTO createOrder = CollectControllerConvert.getCreateOrderDTO(req, memberInfo.getOrganizeId());
        createOrder.setSupMemberId(memberInfo.getSupDirectMemberId());
        createOrder.setCodeMemberId(matchChannel.getCodeMemberId());
        createOrder.setGatewayChannel(gatewayChannel);
        createOrder.setGatewaySubChannel(matchChannel.getGatewaySubChannel());

        OrderInfoDTO orderInfo = orderService.createOrder(createOrder);
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_PLACE_ERROR, "????????????");

        return CollectControllerConvert.getPlaceOrderResp(orderInfo);
    }

}
