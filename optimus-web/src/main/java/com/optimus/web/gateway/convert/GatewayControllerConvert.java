package com.optimus.web.gateway.convert;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.BaseEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.gateway.ScriptEnum;
import com.optimus.util.constants.order.OrderBehaviorEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 网关ControllerConvert
 * 
 * @author sunxp
 */
@Slf4j
public class GatewayControllerConvert {

    /**
     * 获取支付订单DTO
     * 
     * @param orderInfo
     * @param output
     * @return
     */
    public static PayOrderDTO getPayOrderDTO(OrderInfoDTO orderInfo, ExecuteScriptOutputDTO output) {

        // 支付订单DTO
        PayOrderDTO payOrder = new PayOrderDTO();

        payOrder.setId(orderInfo.getId());
        payOrder.setOrderId(output.getOrderId());
        payOrder.setCalleeOrderId(output.getCalleeOrderId());
        payOrder.setOrderAmount(output.getAmount());
        payOrder.setActualAmount(output.getActualAmount());
        payOrder.setOrderStatus(output.getOrderStatus());
        payOrder.setOrderType(orderInfo.getOrderType());
        payOrder.setReleaseStatus(orderInfo.getReleaseStatus());
        payOrder.setMerchantCallbackUrl(orderInfo.getMerchantCallbackUrl());
        payOrder.setMemberId(orderInfo.getMemberId());
        payOrder.setSupMemberId(orderInfo.getSupMemberId());
        payOrder.setCodeMemberId(orderInfo.getCodeMemberId());

        return payOrder;

    }

    /**
     * 获取执行脚本输入DTO
     * 
     * @param gatewaySubChannel
     * @param args
     * @return
     */
    public static ExecuteScriptInputDTO getExecuteScriptInputDTO(GatewaySubChannelDTO gatewaySubChannel, String args) {

        // 执行脚本输入DTO
        ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();

        input.setScriptMethod(ScriptEnum.PARSE.getCode());
        input.setBizContent(gatewaySubChannel.getBizContent());
        input.setImplPath(gatewaySubChannel.getImplPath());
        input.setArgs(args);

        return input;

    }

    /**
     * 获取网关渠道全部参数
     * 
     * @param req
     * @return
     */
    public static String getAllArgs(HttpServletRequest req) {

        // Header参数
        Map<String, Object> headerMap = new HashMap<>(32);

        Enumeration<String> headers = req.getHeaderNames();
        while (headers.hasMoreElements()) {
            String element = headers.nextElement();
            headerMap.put(element, req.getHeader(element));
        }

        // header参数
        String header = JacksonUtil.toString(headerMap);

        // Parameter参数
        String parameter = JacksonUtil.toString(req.getParameterMap());

        // Body参数
        String body = null;
        try {

            body = new String(req.getInputStream().readAllBytes());

        } catch (Exception e) {
            log.error("读取网关渠道参数异常:", e);
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "读取网关渠道参数异常");
        }

        // 合并参数
        Map<String, JsonNode> map = new HashMap<>(16);
        map.put("header", JacksonUtil.toTree(header));
        map.put("parameter", JacksonUtil.toTree(parameter));
        map.put("body", JacksonUtil.toTree(body));

        return JacksonUtil.toString(map);

    }

    /**
     * 获取远程IP
     * 
     * @param req
     * @return
     */
    public static String getRemoteIp(HttpServletRequest req) {

        // Squid
        String ip = req.getHeader("X-Forwarded-For");

        // Apache
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }

        // Weblogic
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }

        // Nginx
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("X-Real-IP");
        }

        // 其他
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }

        // 匹配ip
        if (StringUtils.hasLength(ip)) {
            String[] ips = ip.split(",");

            for (String item : ips) {
                if (BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(item)) {
                    continue;
                }

                ip = item;
                break;
            }
        }

        // 兜底
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }

        return ip;

    }

    /**
     * 获取行为
     * 
     * @param args
     * @return
     */
    public static String getBehavior(String args) {

        // 默认系统触发
        String behavior = OrderBehaviorEnum.BEHAVIOR_S.getCode();

        if (!StringUtils.hasLength(args)) {
            return behavior;
        }

        try {

            // 解析人为触发行为
            JsonNode jsonNode = JacksonUtil.toTree(args);
            jsonNode = jsonNode.get("parameter");
            jsonNode = jsonNode.get("behavior");

            if (Objects.isNull(jsonNode)) {
                return behavior;
            }

            behavior = OrderBehaviorEnum.BEHAVIOR_A.getCode();

        } catch (Exception e) {
            log.error("渠道回调,获取行为异常:", e);
        }

        return behavior;
    }

}
