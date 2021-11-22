package com.optimus.service.gateway.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewayChannelDO;
import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.mapper.GatewayChannelDao;
import com.optimus.dao.mapper.GatewaySubChannelDao;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 网关Service实现
 * 
 * @author sunxp
 */
@Service
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

        GatewaySubChannelDO gatewaySubChannelDO = gatewaySubChannelDao.getGatewaySubChannelBySubChannelCode(channelCode);

        AssertUtil.notEmpty(gatewaySubChannelDO, RespCodeEnum.GATEWAY_CHANNEL_NO, null);

        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelDO, gatewaySubChannel);

        return gatewaySubChannel;

    }

    @Override
    public MatchChannelDTO matchChannel(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel) {

        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), gatewayChannel.getChannelGroup())) {
            return gatewayManager.insideMatch(memberInfo, gatewayChannel);
        }

        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_O.getCode(), gatewayChannel.getChannelGroup())) {
            return gatewayManager.outsideMatch(memberInfo, gatewayChannel);
        }

        return null;

    }

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {
        return gatewayManager.executeScript(input);
    }

}
