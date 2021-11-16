package com.optimus.tps.gateway.impl;

import com.optimus.tps.gateway.GatewayTps;
import com.optimus.tps.gateway.req.ExecuteScriptReq;
import com.optimus.tps.gateway.resp.ExecuteScriptResp;

import org.springframework.stereotype.Component;

/**
 * 网关Tps实现
 * 
 * @author sunxp
 */
@Component
public class GatewayTpsImpl implements GatewayTps {

    @Override
    public ExecuteScriptResp executeScript(ExecuteScriptReq req) {

        return new ExecuteScriptResp();

    }

}
