package com.optimus.tps.gateway;

import com.optimus.tps.gateway.req.ExecuteScriptReq;
import com.optimus.tps.gateway.resp.ExecuteScriptResp;

/**
 * 网关Tps
 * 
 * @author sunxp
 */
public interface GatewayTps {

    /**
     * 执行脚本
     * 
     * @param req
     * @return
     */
    ExecuteScriptResp executeScript(ExecuteScriptReq req);

}
