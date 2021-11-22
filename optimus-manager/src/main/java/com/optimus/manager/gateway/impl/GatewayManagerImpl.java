package com.optimus.manager.gateway.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.MemberAgentCodeRelDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.mapper.GatewaySubChannelDao;
import com.optimus.dao.mapper.MemberAgentCodeRelDao;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.convert.GatewayManagerConvert;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;

import org.springframework.stereotype.Component;

/**
 * 网关Manager实现
 * 
 * @author sunxp
 */
@Component
public class GatewayManagerImpl implements GatewayManager {

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private GatewaySubChannelDao gatewaySubChannelDao;

    @Resource
    private MemberAgentCodeRelDao memberAgentCodeRelDao;

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {

        return new ExecuteScriptOutputDTO();

    }

    @Override
    public MatchChannelDTO insideMatch(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel) {

        // 查询商户费率
        List<String> memberIdList = Arrays.asList(memberInfo.getMemberId());
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdListAndChannelCodes(memberIdList, gatewayChannel.getChannelCode());
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "会员渠道未配置");

        // 查询所有可用的子渠道
        List<GatewaySubChannelDO> gatewaySubChannelList = gatewaySubChannelDao.listGatewaySubChannelByParentChannelCodeAndChannelStatuss(gatewayChannel.getChannelCode(), GatewayChannelStatusEnum.GATEWAY_CHANNEL_STATUS_Y.getCode());
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到有效的渠道");

        // 获取一个子渠道实例
        GatewaySubChannelDO gatewaySubChannel = GatewayManagerConvert.getGatewaySubChannelDO(gatewaySubChannelList);
        AssertUtil.notEmpty(gatewaySubChannel, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到子渠道实例");

        // 选取商户费率和子渠道
        MatchChannelDTO matchChannel = GatewayManagerConvert.getMatchChannelDTO(memberChannelList, gatewaySubChannel, gatewayChannel.getChannelGroup());
        AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到商户费率和子渠道");

        return matchChannel;

    }

    @Override
    public MatchChannelDTO outsideMatch(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel) {

        // 查询代理的一级码商列表
        List<MemberAgentCodeRelDO> memberAgentCodeRelList = memberAgentCodeRelDao.listMemberAgentCodeRelByMemberIdAndMemberLevels(memberInfo.getSupDirectMemberId(), BigDecimal.ONE.toString());
        AssertUtil.notEmpty(memberAgentCodeRelList, RespCodeEnum.MEMBER_ERROR, "会员代理码商关系未配置");

        // 查询会员渠道列表
        List<String> memberIdList = memberAgentCodeRelList.stream().map(MemberAgentCodeRelDO::getSubMemberId).distinct().collect(Collectors.toList());
        memberIdList.add(memberInfo.getMemberId());
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdListAndChannelCodes(memberIdList, gatewayChannel.getChannelCode());
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "会员渠道未配置");

        // 获取符合费率的会员渠道列表
        memberChannelList = GatewayManagerConvert.getMemberChannelDTOList(memberInfo, memberChannelList);
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "会员渠道不符合费率条件");

        // 查询可用子渠道
        List<String> subChannelCodeList = memberChannelList.stream().map(MemberChannelDO::getSubChannelCode).distinct().collect(Collectors.toList());
        List<GatewaySubChannelDO> gatewaySubChannelList = gatewaySubChannelDao.listGatewaySubChannelBySubChannelCodeListAndChannelStatuss(subChannelCodeList, GatewayChannelStatusEnum.GATEWAY_CHANNEL_STATUS_Y.getCode());
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到有效的渠道");

        // 获取一个子渠道实例
        GatewaySubChannelDO gatewaySubChannel = GatewayManagerConvert.getGatewaySubChannelDO(gatewaySubChannelList);
        AssertUtil.notEmpty(gatewaySubChannel, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到子渠道实例");

        // 获取一级码商及所有下级
        List<String> ancestorMemberIdList = memberChannelList.stream().map(MemberChannelDO::getMemberId).distinct().collect(Collectors.toList());
        memberAgentCodeRelList = memberAgentCodeRelDao.listMemberAgentCodeRelByMemberIdAndAncestorMemberIdLists(memberInfo.getSupDirectMemberId(), ancestorMemberIdList);
        AssertUtil.notEmpty(memberAgentCodeRelList, RespCodeEnum.MEMBER_ERROR, "会员代理码商关系未配置");

        // 获取一个子渠道实例下的码商集合
        memberChannelList = memberChannelDao.listMemberChannelByMemberIdListAndSubChannelCodes(ancestorMemberIdList, gatewaySubChannel.getChannelCode());

        // 选取码商和子渠道
        MatchChannelDTO matchChannel = GatewayManagerConvert.getMatchChannelDTO(memberChannelList, gatewaySubChannel, gatewayChannel.getChannelGroup());
        AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到码商和子渠道");
        return matchChannel;

    }

}
