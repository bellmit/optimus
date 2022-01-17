package com.optimus.service.gateway.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewayChannelDO;
import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.mapper.GatewayChannelDao;
import com.optimus.dao.mapper.GatewaySubChannelDao;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.dao.query.GatewaySubChannelQuery;
import com.optimus.dao.query.MemberChannelQuery;
import com.optimus.dao.query.MemberInfoQuery;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.convert.GatewayManagerConvert;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.convert.MemberManagerConvert;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.service.gateway.GatewayService;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.model.exception.OptimusException;

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
    private MemberInfoDao memberInfoDao;

    @Resource
    private MemberChannelDao memberChannelDao;

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

        log.info("匹配渠道,会员信息:{},网关渠道:{},金额:{}", memberInfo, gatewayChannel, amount);

        // 查询父渠道下启用的子渠道
        GatewaySubChannelQuery gatewaySubChannelQuery = GatewayManagerConvert.getGatewaySubChannelQuery(memberInfo, gatewayChannel);
        List<GatewaySubChannelDO> gatewaySubChannelList = gatewaySubChannelDao.listGatewaySubChannelByGatewaySubChannelQuerys(gatewaySubChannelQuery);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "无启用的子渠道");

        // 查询代理会员渠道
        MemberChannelQuery memberChannelQuery = MemberManagerConvert.getMemberChannelQuery(memberInfo, gatewaySubChannelList.stream().map(GatewaySubChannelDO::getChannelCode).distinct().collect(Collectors.toList()));
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberChannelQuerys(memberChannelQuery);
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "无有效的会员渠道");

        // 查询有效性的码商会员
        MemberInfoQuery memberInfoQuery = MemberManagerConvert.getMemberInfoQuery(memberChannelList);
        List<MemberInfoDO> memberInfoList = memberInfoDao.listMemberInfoByMemberInfoQuerys(memberInfoQuery);
        AssertUtil.notEmpty(memberInfoList, RespCodeEnum.MEMBER_ERROR, "无有效的会员");

        // 真实代理会员渠道
        memberChannelList = MemberManagerConvert.getMemberChannelList(memberInfoList, memberChannelList);
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "未匹配到会员渠道");

        // 真实子渠道
        gatewaySubChannelList = GatewayManagerConvert.getGatewaySubChannelList(memberChannelList, gatewaySubChannelList);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "未匹配到子渠道");

        // 筛选子渠道
        gatewaySubChannelList = GatewayManagerConvert.getGatewaySubChannelList(gatewaySubChannelList, amount);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "无符合条件的子渠道");

        // 自研渠道匹配
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_I.getCode(), gatewayChannel.getChannelGroup())) {

            MatchChannelDTO matchChannel = GatewayManagerConvert.getMatchChannelDTO(gatewaySubChannelList);
            AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "未匹配到子渠道");
            log.info("已匹配渠道内部:{}", matchChannel);

            return matchChannel;
        }

        // 外部渠道匹配
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_O.getCode(), gatewayChannel.getChannelGroup())) {

            MatchChannelDTO matchChannel = GatewayManagerConvert.getMatchChannelDTO(memberInfoList, memberChannelList, gatewaySubChannelList);
            AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_ERROR, "未匹配到子渠道");
            log.info("已匹配渠道外部:{}", matchChannel);

            return matchChannel;
        }

        // 网关渠道分组不合法
        throw new OptimusException(RespCodeEnum.GATEWAY_CHANNEL_ERROR, "网关渠道分组不合法");
    }

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {
        return gatewayManager.executeScript(input);
    }

}
