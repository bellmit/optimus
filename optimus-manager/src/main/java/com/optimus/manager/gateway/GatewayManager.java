package com.optimus.manager.gateway;

import com.optimus.manager.gateway.dto.InputChannelMessageDTO;
import com.optimus.manager.gateway.dto.OutputChannelMessageDTO;

/**
 * 网关Manager
 * 
 * @author sunxp
 */
public interface GatewayManager {

    /**
     * 解析渠道消息
     * 
     * @param input
     * @return
     */
    OutputChannelMessageDTO analysisChannelMessage(InputChannelMessageDTO input);

}
