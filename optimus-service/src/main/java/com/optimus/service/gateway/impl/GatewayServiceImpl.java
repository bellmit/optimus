package com.optimus.service.gateway.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewayChannelDO;
import com.optimus.dao.mapper.GatewayChannelDao;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.GatewayChannelDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 网关Service实现
 * 
 * @author sunxp
 */
@Service
public class GatewayServiceImpl implements GatewayService {

    @Resource
    private GatewayChannelDao gatewayChannelDao;

    @Override
    public GatewayChannelDTO getGatewayChannelByChannelCode(String channelCode) {

        GatewayChannelDO gatewayChannelDO = gatewayChannelDao.getGatewayChannelByChannelCode(channelCode);

        AssertUtil.notEmpty(gatewayChannelDO, RespCodeEnum.GATEWAY_CHANNEL_NO, null);

        GatewayChannelDTO gatewayChannel = new GatewayChannelDTO();
        BeanUtils.copyProperties(gatewayChannelDO, gatewayChannel);

        return gatewayChannel;

    }

    @Override
    public String matchChannel(GatewayChannelDTO gatewayChannel) {
        return null;
    }

}
