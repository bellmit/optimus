package com.optimus.manager.gateway.impl;

import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.convert.GatewayManagerConvert;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.tps.gateway.GatewayTps;
import com.optimus.tps.gateway.req.ExecuteScriptReq;
import com.optimus.tps.gateway.resp.ExecuteScriptResp;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 网关Manager实现
 * 
 * @author sunxp
 */
@Component
public class GatewayManagerImpl implements GatewayManager {

    @Autowired
    private GatewayTps gatewayTps;

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {

        ExecuteScriptReq req = GatewayManagerConvert.getExecuteScriptReq(input);

        ExecuteScriptResp resp = gatewayTps.executeScript(req);
        AssertUtil.notEmpty(resp, RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, null);

        return GatewayManagerConvert.getExecuteScriptOutputDTO(resp);

    }

}
