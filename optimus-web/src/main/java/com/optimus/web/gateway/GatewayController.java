package com.optimus.web.gateway;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.member.MemberService;
import com.optimus.service.order.OrderService;
import com.optimus.util.AssertUtil;
import com.optimus.util.annotation.OptimusRateLimiter;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.order.OrderBehaviorEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.gateway.convert.GatewayControllerConvert;
import com.optimus.web.gateway.validate.GatewayControllerValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关Controller
 *
 * @author sunxp
 */
@RestController
@RequestMapping(value = "/optimus/gateway")
public class GatewayController {

    @Autowired
    public MemberService memberService;

    @Autowired
    public GatewayService gatewayService;

    @Autowired
    public OrderService orderService;

    /**
     * 渠道回调
     *
     * @return
     */
    @OptimusRateLimiter(permits = 500D, timeout = 0)
    @RequestMapping(value = "/channelCallback", method = { RequestMethod.GET, RequestMethod.POST })
    public String channelCallback(HttpServletRequest req, @RequestParam("subChannelCode") String subChannelCode) {

        // 商户客户端IP
        String ip = GatewayControllerConvert.getRemoteIp(req);
        AssertUtil.notEmpty(ip, RespCodeEnum.ERROR_IP, "客户端IP不能为空");

        // 查询子渠道
        GatewaySubChannelDTO gatewaySubChannel = gatewayService.getGatewaySubChannelBySubChannelCode(subChannelCode);
        AssertUtil.notEmpty(gatewaySubChannel, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "网关子渠道不存在");

        // 验证IP
        String[] ips = gatewaySubChannel.getCallbackIp().split(",");
        if (!Arrays.asList(ips).contains(ip)) {
            throw new OptimusException(RespCodeEnum.ERROR_IP, "客户端IP异常");
        }

        // 执行脚本输入DTO
        ExecuteScriptInputDTO input = GatewayControllerConvert.getExecuteScriptInputDTO(gatewaySubChannel);
        input.setArgs(GatewayControllerConvert.getAllArgs(req));

        // 执行脚本
        ExecuteScriptOutputDTO output = gatewayService.executeScript(input);
        GatewayControllerValidate.validateChannelCallback(output);

        // 查询订单信息:订单类型/子渠道合法性
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(output.getOrderId());
        AssertUtil.notEquals(OrderTypeEnum.ORDER_TYPE_C.getCode(), orderInfo.getOrderType(), RespCodeEnum.ORDER_ERROR, "订单类型异常");
        AssertUtil.notEquals(orderInfo.getSubChannelCode(), subChannelCode, RespCodeEnum.ORDER_ERROR, "订单子渠道异常");
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus(), RespCodeEnum.ORDER_ERROR, "原订单状态不合法");

        // 支付
        PayOrderDTO payOrder = GatewayControllerConvert.getPayOrderDTO(output, orderInfo);
        payOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        payOrder.setBehavior(OrderBehaviorEnum.BEHAVIOR_S.getCode());

        orderService.payOrder(payOrder);

        return output.getContent();
    }

}