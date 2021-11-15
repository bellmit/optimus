package com.optimus.web.gateway.convert;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

/**
 * 网关Controller转换器
 * 
 * @author sunxp
 */
public class GatewayControllerConvert {

    /**
     * 获取网关渠道全部参数
     * 
     * @param req
     * @return
     */
    public static String getAllParameter(HttpServletRequest req) {

        // 获取Header参数
        Map<String, Object> headerMap = new HashMap<>(32);

        Enumeration<String> headers = req.getHeaderNames();
        while (headers.hasMoreElements()) {

            String element = headers.nextElement();
            headerMap.put(element, req.getHeader(element));

        }

        String header = JacksonUtil.toString(headerMap);

        // 获取Parameter参数
        String parameter = JacksonUtil.toString(req.getParameterMap());

        // 获取Body参数
        String body = null;
        try {

            body = new String(req.getInputStream().readAllBytes());

        } catch (Exception e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "转换Body参数异常");
        }

        // 合并参数
        Map<String, JsonNode> map = new HashMap<>(16);
        map.put("header", JacksonUtil.toTree(header));
        map.put("parameter", JacksonUtil.toTree(parameter));
        map.put("body", JacksonUtil.toTree(body));

        return JacksonUtil.toString(map);

    }

}
