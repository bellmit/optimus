package com.optimus.service.gateway.impl;

import java.math.BigDecimal;

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
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 网关ServiceImpl
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

        log.info("根据渠道编号查询网关渠道,渠道编号:{}", channelCode);

        // 根据渠道编号查询网关渠道
        GatewayChannelDO gatewayChannelDO = gatewayChannelDao.getGatewayChannelByChannelCode(channelCode);
        AssertUtil.notEmpty(gatewayChannelDO, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "网关渠道不存在");

        // 网关渠道DTO
        GatewayChannelDTO gatewayChannel = new GatewayChannelDTO();
        BeanUtils.copyProperties(gatewayChannelDO, gatewayChannel);

        log.info("网关渠道:{}", gatewayChannel);

        return gatewayChannel;

    }

    @Override
    public GatewaySubChannelDTO getGatewaySubChannelBySubChannelCode(String channelCode) {

        log.info("根据子渠道编号查询网关子渠道,子渠道编号:{}", channelCode);

        // 根据网关子渠道编号查询网关子渠道
        GatewaySubChannelDO gatewaySubChannelDO = gatewaySubChannelDao.getGatewaySubChannelBySubChannelCode(channelCode);
        AssertUtil.notEmpty(gatewaySubChannelDO, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "网关子渠道不存在");

        // 网关子渠道DTO
        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelDO, gatewaySubChannel);

        log.info("网关子渠道:{}", gatewaySubChannel);

        return gatewaySubChannel;

    }

    @Override
    public MatchChannelDTO matchChannel(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel, BigDecimal amount) {

        // 自研渠道匹配
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_I.getCode(), gatewayChannel.getChannelGroup())) {
            return gatewayManager.insideMatch(memberInfo, gatewayChannel, amount);
        }

        // 外部渠道匹配
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_O.getCode(), gatewayChannel.getChannelGroup())) {
            return gatewayManager.outsideMatch(memberInfo, gatewayChannel, amount);
        }

        // 网关渠道分组不合法
        throw new OptimusException(RespCodeEnum.GATEWAY_CHANNEL_ERROR, "网关渠道分组不合法");

    }

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {
        return gatewayManager.executeScript(input);
    }

}
