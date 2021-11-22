package com.optimus.manager.gateway;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;

/**
 * 网关Manager
 * 
 * @author sunxp
 */
public interface GatewayManager {

    /**
     * 执行脚本
     * 
     * @param input
     * @return
     */
    ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input);

    /**
     * 内部匹配
     * 
     * @param memberInfo
     * @param gatewayChannel
     * @return
     */
    MatchChannelDTO insideMatch(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel);

    /**
     * 外部匹配
     * 
     * @param memberInfo
     * @param gatewayChannel
     * @return
     */
    MatchChannelDTO outsideMatch(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel);

}
