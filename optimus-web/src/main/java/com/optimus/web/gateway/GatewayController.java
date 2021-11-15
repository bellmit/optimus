package com.optimus.web.gateway;

import javax.servlet.http.HttpServletRequest;

import com.optimus.web.gateway.convert.GatewayControllerConvert;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 网关Controller
 * 
 * @author sunxp
 */
@RestController
@RequestMapping(value = "/optimus/gateway")
@Slf4j
public class GatewayController {

    /**
     * 渠道回调
     * 
     * @return
     */
    @RequestMapping(value = "/channelCallback", method = { RequestMethod.GET, RequestMethod.POST })
    public String channelCallback(HttpServletRequest req) {

        // 处理参数
        String parameter = GatewayControllerConvert.getAllParameter(req);
        log.info("channelCallback parameter is {}", parameter);

        // 查询子渠道并验证调用IP

        // 调用脚本解析渠道参数为模版对象

        // 验证订单及子渠道合法性

        // 若成功，则更新订单信息

        // 异步分润

        // 异步通知商户

        return "OK";

    }

}
