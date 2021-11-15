package com.optimus.tps.gateway.impl;

import com.optimus.tps.gateway.GatewayTps;
import com.optimus.tps.gateway.resp.AnalysisChannelMessageResp;

import org.springframework.stereotype.Component;

/**
 * 网关Tps实现
 * 
 * @author sunxp
 */
@Component
public class GatewayTpsImpl implements GatewayTps {

    @Override
    public AnalysisChannelMessageResp analysisChannelMessage(String message) {

        return new AnalysisChannelMessageResp();

    }

}
