package com.optimus.manager.gateway;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;

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

}
