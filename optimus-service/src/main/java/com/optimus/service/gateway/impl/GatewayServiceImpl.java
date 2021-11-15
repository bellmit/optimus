package com.optimus.service.gateway.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewayChannelDO;
import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.mapper.GatewayChannelDao;
import com.optimus.dao.mapper.GatewaySubChannelDao;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.AnalysisChannelMessageDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.GatewayChannelDTO;
import com.optimus.service.gateway.dto.GatewaySubChannelDTO;
import com.optimus.service.gateway.dto.HandleForChannelCallbackDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 网关Service实现
 * 
 * @author sunxp
 */
@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    private GatewayManager gatewayManager;

    @Resource
    private GatewayChannelDao gatewayChannelDao;

    @Resource
    private GatewaySubChannelDao gatewaySubChannelDao;

    @Override
    public GatewayChannelDTO getGatewayChannelByChannelCode(String channelCode) {

        GatewayChannelDO gatewayChannelDO = gatewayChannelDao.getGatewayChannelByChannelCode(channelCode);

        AssertUtil.notEmpty(gatewayChannelDO, RespCodeEnum.GATEWAY_CHANNEL_NO, null);

        GatewayChannelDTO gatewayChannel = new GatewayChannelDTO();
        BeanUtils.copyProperties(gatewayChannelDO, gatewayChannel);

        return gatewayChannel;

    }

    @Override
    public GatewaySubChannelDTO getGatewaySubChannelBySubChannelCode(String channelCode) {

        GatewaySubChannelDO gatewaySubChannelDO = gatewaySubChannelDao
                .getGatewaySubChannelBySubChannelCode(channelCode);

        AssertUtil.notEmpty(gatewaySubChannelDO, RespCodeEnum.GATEWAY_CHANNEL_NO, null);

        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelDO, gatewaySubChannel);

        return gatewaySubChannel;

    }

    @Override
    public String matchChannel(GatewayChannelDTO gatewayChannel) {
        return null;
    }

    @Override
    public String handleForChannelCallback(HandleForChannelCallbackDTO handleForChannelCallback) {

        // 调用脚本解析渠道参数为模版对象
        AnalysisChannelMessageDTO analysisChannelMessage = gatewayManager
                .analysisChannelMessage(handleForChannelCallback.getMessage());
        log.info("analysisChannelMessage is {}", analysisChannelMessage);

        // 验证订单及子渠道合法性

        // 若成功，则更新订单信息

        // 异步分润

        // 异步通知商户

        return analysisChannelMessage.getMessage();

    }

}
