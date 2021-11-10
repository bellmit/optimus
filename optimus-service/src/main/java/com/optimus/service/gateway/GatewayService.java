package com.optimus.service.gateway;

import com.optimus.service.gateway.dto.MatchChannelDTO;

/**
 * GatewayService
 * 
 * @author sunxp
 */
public interface GatewayService {

    /**
     * 匹配渠道
     * 
     * @param matchChannelDTO 匹配渠道DTO
     * @return 子渠道编号
     */
    String matchChannel(MatchChannelDTO matchChannelDTO);

}
