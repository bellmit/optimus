package com.optimus.service.gateway.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewayChannelDO;
import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.mapper.GatewayChannelDao;
import com.optimus.dao.mapper.GatewaySubChannelDao;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.service.gateway.dto.GatewayChannelDTO;
import com.optimus.service.gateway.dto.GatewaySubChannelDTO;
import com.optimus.service.gateway.dto.MatchChannelDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        GatewaySubChannelDO gatewaySubChannelDO = gatewaySubChannelDao.getGatewaySubChannelBySubChannelCode(channelCode);

        AssertUtil.notEmpty(gatewaySubChannelDO, RespCodeEnum.GATEWAY_CHANNEL_NO, null);

        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelDO, gatewaySubChannel);

        return gatewaySubChannel;

    }

    @Override
    public MatchChannelDTO matchChannel(GatewayChannelDTO gatewayChannel, String memberId) {

        MatchChannelDTO matchChannel = new MatchChannelDTO();

        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), gatewayChannel.getChannelGroup())) {
            return matchChannel;
        }

        // 查询码商列表

        // 查询子渠道列表

        // 选取子渠道

        AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "未匹配到子通道");

        return matchChannel;

    }

    @Override
    public String handleForChannelCallback(ExecuteScriptInputDTO input) {

        // 执行脚本
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(input);
        log.info("analysisChannelMessage is {}", output);

        // 验证订单及子渠道合法性

        // 支付订单

        // 异步分润

        // 异步通知商户

        return output.getMemo();

    }

}
