package com.optimus.web.gateway;

import javax.servlet.http.HttpServletRequest;

import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.GatewaySubChannelDTO;
import com.optimus.service.gateway.dto.HandleForChannelCallbackDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.web.gateway.convert.GatewayControllerConvert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    public GatewayService gatewayService;

    /**
     * 渠道回调
     *
     * @return
     */
    @RequestMapping(value = "/channelCallback", method = { RequestMethod.GET, RequestMethod.POST })
    public String channelCallback(HttpServletRequest req, @RequestParam("subChannelCode") String subChannelCode) {

        // 获取远程客户端IP
        String ip = GatewayControllerConvert.getRemoteIp(req);
        AssertUtil.notEmpty(ip, RespCodeEnum.INVALID_IP, "远程客户端IP不能为空");

        // 查询子渠道并验证调用IP
        GatewaySubChannelDTO gatewaySubChannel = gatewayService.getGatewaySubChannelBySubChannelCode(subChannelCode);

        // 处理参数
        String parameter = GatewayControllerConvert.getAllParameter(req);
        log.info("channelCallback parameter is {}", parameter);

        // 处理渠道回调
        HandleForChannelCallbackDTO handleForChannelCallback = GatewayControllerConvert
                .getHandleForChannelCallbackDTO(gatewaySubChannel);
        handleForChannelCallback.setIp(ip);
        handleForChannelCallback.setMessage(parameter);

        return gatewayService.handleForChannelCallback(handleForChannelCallback);

    }

}