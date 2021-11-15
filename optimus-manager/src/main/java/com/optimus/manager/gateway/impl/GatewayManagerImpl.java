package com.optimus.manager.gateway.impl;

import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.AnalysisChannelMessageDTO;
import com.optimus.tps.gateway.GatewayTps;
import com.optimus.tps.gateway.resp.AnalysisChannelMessageResp;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 网关Manager实现
 * 
 * @author sunxp
 */
public class GatewayManagerImpl implements GatewayManager {

    @Autowired
    private GatewayTps gatewayTps;

    @Override
    public AnalysisChannelMessageDTO analysisChannelMessage(String message) {

        AnalysisChannelMessageResp analysisChannelMessageResp = gatewayTps.analysisChannelMessage(message);
        AssertUtil.notEmpty(analysisChannelMessageResp, RespCodeEnum.GATEWAY_ANALYSIS_ERROR, null);

        AnalysisChannelMessageDTO analysisChannelMessage = new AnalysisChannelMessageDTO();
        BeanUtils.copyProperties(analysisChannelMessageResp, analysisChannelMessage);

        return analysisChannelMessage;

    }

}
