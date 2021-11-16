package com.optimus.tps.gateway;

import com.optimus.tps.gateway.req.AnalysisChannelMessageReq;
import com.optimus.tps.gateway.resp.AnalysisChannelMessageResp;

/**
 * 网关Tps
 * 
 * @author sunxp
 */
public interface GatewayTps {

    /**
     * 解析渠道消息
     * 
     * @param req
     * @return
     */
    AnalysisChannelMessageResp analysisChannelMessage(AnalysisChannelMessageReq req);

}
