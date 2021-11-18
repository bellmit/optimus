package com.optimus.service.gateway;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.service.gateway.dto.GatewayChannelDTO;
import com.optimus.service.gateway.dto.GatewaySubChannelDTO;
import com.optimus.service.gateway.dto.MatchChannelDTO;

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
     * @param gatewayChannel
     * @param memberId
     * @return
     */
    MatchChannelDTO matchChannel(GatewayChannelDTO gatewayChannel, String memberId);

    /**
     * 执行脚本
     * 
     * @param input
     * @return
     */
    ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input);

}
