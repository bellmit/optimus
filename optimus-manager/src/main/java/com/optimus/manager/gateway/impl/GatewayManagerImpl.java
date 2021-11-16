package com.optimus.manager.gateway.impl;

import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.InputChannelMessageDTO;
import com.optimus.manager.gateway.dto.OutputChannelMessageDTO;
import com.optimus.tps.gateway.GatewayTps;
import com.optimus.tps.gateway.req.AnalysisChannelMessageReq;
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
    public OutputChannelMessageDTO analysisChannelMessage(InputChannelMessageDTO input) {

        AnalysisChannelMessageReq req = new AnalysisChannelMessageReq();
        BeanUtils.copyProperties(input, req);

        AnalysisChannelMessageResp resp = gatewayTps.analysisChannelMessage(req);
        AssertUtil.notEmpty(resp, RespCodeEnum.GATEWAY_ANALYSIS_ERROR, null);

        OutputChannelMessageDTO message = new OutputChannelMessageDTO();
        BeanUtils.copyProperties(resp, message);

        return message;

    }

}
