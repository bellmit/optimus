package com.optimus.service.gateway;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;

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
     * @param memberInfo
     * @param gatewayChannel
     * @return
     */
    MatchChannelDTO matchChannel(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel);

    /**
     * 执行脚本
     * 
     * @param input
     * @return
     */
    ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input);

}
