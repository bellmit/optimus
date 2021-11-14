package com.optimus.service.gateway;

import com.optimus.service.gateway.dto.GatewayChannelDTO;

/**
 * 网关服务
 * 
 * @author sunxp
 */
public interface GatewayService {

    /**
     * 根据渠道编号查询网关渠道
     * 
     * @param channelCode
     * @return
     */
    GatewayChannelDTO getGatewayChannelByChannelCode(String channelCode);

    /**
     * 匹配渠道
     * 
     * @param gatewayChannel 匹配渠道DTO
     * @return 子渠道编号
     */
    String matchChannel(GatewayChannelDTO gatewayChannel);

}
