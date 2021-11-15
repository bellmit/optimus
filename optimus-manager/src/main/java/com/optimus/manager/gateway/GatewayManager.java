package com.optimus.manager.gateway;

import com.optimus.manager.gateway.dto.AnalysisChannelMessageDTO;

/**
 * 网关Manager
 * 
 * @author sunxp
 */
public interface GatewayManager {

    /**
     * 解析渠道消息
     * 
     * @param message
     * @return
     */
    AnalysisChannelMessageDTO analysisChannelMessage(String message);

}
