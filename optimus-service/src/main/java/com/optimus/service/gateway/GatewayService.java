package com.optimus.service.gateway;

import com.optimus.service.gateway.dto.GatewayChannelDTO;
import com.optimus.service.gateway.dto.GatewaySubChannelDTO;
import com.optimus.service.gateway.dto.HandleForChannelCallbackDTO;

/**
 * 网关Service
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
     * 根据子渠道编号查询网关子渠道
     * 
     * @param channelCode
     * @return
     */
    GatewaySubChannelDTO getGatewaySubChannelBySubChannelCode(String channelCode);

    /**
     * 匹配渠道
     * 
     * @param gatewayChannel 匹配渠道DTO
     * @return 子渠道编号
     */
    String matchChannel(GatewayChannelDTO gatewayChannel);

    /**
     * 处理渠道回调
     * 
     * @param handleForChannelCallback
     * @return
     */
    String handleForChannelCallback(HandleForChannelCallbackDTO handleForChannelCallback);

}
