package com.optimus.manager.gateway.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.MemberInfoDO;
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
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * 网关ManagerImpl
 *
 * @author sunxp
 */
@Component
@Slf4j
public class GatewayManagerImpl implements GatewayManager {

    @Autowired
    private GroovyScriptEngine groovyScriptEngine;

    @Resource
    private MemberInfoDao memberInfoDao;

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private GatewaySubChannelDao gatewaySubChannelDao;

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {

        try {

            // 绑定参数
            Binding binding = new Binding();
            binding.setVariable("args", JacksonUtil.toString(input));

            // 执行脚本
            Object result = groovyScriptEngine.run(input.getImplPath(), binding);
            AssertUtil.notEmpty(result, RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本输出对象不能为空");

            // 脚本输出对象
            return JacksonUtil.toBean((String) result, ExecuteScriptOutputDTO.class);

        } catch (OptimusException e) {
            log.error("执行脚本异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return null;
        } catch (Exception e) {
            log.error("执行脚本异常:", e);
            return null;
        }

    }

    @Override
    public MatchChannelDTO insideMatch(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel, BigDecimal amount) {

        // 查询代理当前渠道下启用的子渠道List
        GatewaySubChannelQuery gatewaySubChannelQuery = GatewayManagerConvert.getGatewaySubChannelQuery(memberInfo, gatewayChannel);
        List<GatewaySubChannelDO> gatewaySubChannelList = gatewaySubChannelDao.listGatewaySubChannelByGatewaySubChannelQuerys(gatewaySubChannelQuery);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_NO, "代理无启用的子渠道");

        // 筛选子渠道
        gatewaySubChannelList = GatewayManagerConvert.getGatewaySubChannelList(gatewaySubChannelList, amount);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_NO, "无符合条件的子渠道");

        // 选择子渠道
        MatchChannelDTO matchChannel = GatewayManagerConvert.getMatchChannelDTO(gatewaySubChannelList);
        AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到子渠道");

        return matchChannel;

    }

    @Override
    public MatchChannelDTO outsideMatch(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel, BigDecimal amount) {

        // 查询代理当前渠道下启用的子渠道List
        GatewaySubChannelQuery gatewaySubChannelQuery = GatewayManagerConvert.getGatewaySubChannelQuery(memberInfo, gatewayChannel);
        List<GatewaySubChannelDO> gatewaySubChannelList = gatewaySubChannelDao.listGatewaySubChannelByGatewaySubChannelQuerys(gatewaySubChannelQuery);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_NO, "代理无启用的子渠道");

        // 筛选子渠道
        gatewaySubChannelList = GatewayManagerConvert.getGatewaySubChannelList(gatewaySubChannelList, amount);
        AssertUtil.notEmpty(gatewaySubChannelList, RespCodeEnum.GATEWAY_CHANNEL_NO, "无符合条件的子渠道");

        // 查询代理在子渠道下配置的码商会员渠道List
        MemberChannelQuery memberChannelQuery = GatewayManagerConvert.getMemberChannelQuery(memberInfo, gatewaySubChannelList);
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelTopByMemberChannelQuerys(memberChannelQuery);
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "无会员渠道");

        // 查询码商会员的有效性
        MemberInfoQuery memberInfoQuery = GatewayManagerConvert.getMemberInfoQuery(memberChannelList);
        List<MemberInfoDO> memberInfoList = memberInfoDao.listMemberInfoByMemberInfoQuerys(memberInfoQuery);
        AssertUtil.notEmpty(memberInfoList, RespCodeEnum.MEMBER_ERROR, "会员信息状态已删除或无效");

        // 选择码商和子渠道
        MatchChannelDTO matchChannel = GatewayManagerConvert.getMatchChannelDTO(memberInfoList, memberChannelList, gatewaySubChannelList);
        AssertUtil.notEmpty(matchChannel, RespCodeEnum.GATEWAY_CHANNEL_NO, "未匹配到子渠道");
        return matchChannel;

    }

}
