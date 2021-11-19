package com.optimus.manager.gateway.impl;

import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;

import org.springframework.stereotype.Component;

/**
 * 网关Manager实现
 * 
 * @author sunxp
 */
@Component
public class GatewayManagerImpl implements GatewayManager {

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {

        return new ExecuteScriptOutputDTO();

    }

}
