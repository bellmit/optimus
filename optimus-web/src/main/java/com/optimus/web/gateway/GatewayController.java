package com.optimus.web.gateway;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.GatewaySubChannelDTO;
import com.optimus.service.order.OrderService;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.order.OrderBehaviorEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.gateway.convert.GatewayControllerConvert;

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
    public GatewayService gatewayService;

    @Autowired
    public OrderService orderService;

    /**
     * 渠道回调
     *
     * @return
     */
    @RequestMapping(value = "/channelCallback", method = { RequestMethod.GET, RequestMethod.POST })
    public String channelCallback(HttpServletRequest req, @RequestParam("subChannelCode") String subChannelCode) {

        // 获取商户客户端IP
        String ip = GatewayControllerConvert.getRemoteIp(req);
        AssertUtil.notEmpty(ip, RespCodeEnum.INVALID_IP, "客户端IP不能为空");

        // 查询子渠道
        GatewaySubChannelDTO gatewaySubChannel = gatewayService.getGatewaySubChannelBySubChannelCode(subChannelCode);

        // 验证IP
        String[] ips = gatewaySubChannel.getCallbackIp().split(",");
        if (!Arrays.asList(ips).contains(ip)) {
            throw new OptimusException(RespCodeEnum.INVALID_IP, "调用方IP异常");
        }

        // 执行脚本
        ExecuteScriptInputDTO input = GatewayControllerConvert.getExecuteScriptInputDTO(gatewaySubChannel);
        input.setArgs(GatewayControllerConvert.getAllArgs(req));

        ExecuteScriptOutputDTO output = gatewayService.executeScript(input);

        // 验证子渠道合法性
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderId(output.getOrderId());
        AssertUtil.notEquals(orderInfo.getSubChannelCode(), subChannelCode, RespCodeEnum.ORDER_ERROR, "订单子渠道信息异常");

        // 支付
        PayOrderDTO payOrder = GatewayControllerConvert.getPayOrderDTO(output);
        payOrder.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        payOrder.setBehavior(OrderBehaviorEnum.ORDER_BEHAVIOR_S.getCode());
        payOrder.setMemberId(orderInfo.getMemberId());
        payOrder.setSupMemberId(orderInfo.getSupMemberId());
        payOrder.setCodeMemberId(orderInfo.getCodeMemberId());

        orderService.payOrder(payOrder);

        return output.getMemo();

    }

}
