package com.optimus.manager.member.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.domain.MemberTransConfineDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.dao.mapper.MemberTransConfineDao;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberCollectFeeTypeEnum;
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.member.MemberWithdrawFeeSwitchEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户managerImpl
 *
 * @author hongp
 */
@Component
@Slf4j
public class MemberManagerImpl implements MemberManager {

    @Resource
    private CommonSystemConfigDao commonSystemConfigDao;

    @Resource
    private MemberInfoDao memberInfoDao;

    @Resource
    private MemberTransConfineDao memberTransConfineDao;

    @Override
    public BigDecimal getFeeForWithdraw(BigDecimal orderAmount, MemberTransConfineDTO memberTransConfine) {

        log.info("获取提现手续费订单金额:{},会员交易限制:{}", orderAmount, memberTransConfine);

        // 验证会员交易限制
        if (!StringUtils.pathEquals(MemberWithdrawFeeSwitchEnum.WITHDRAW_FEE_SWITCH_Y.getCode(), memberTransConfine.getWithdrawFeeSwitch())) {
            return BigDecimal.ZERO;
        }

        // 单笔
        if (StringUtils.pathEquals(MemberCollectFeeTypeEnum.COLLECT_FEE_TYPE_S.getCode(), memberTransConfine.getCollectFeeType())) {
            return memberTransConfine.getSingleCollectFee();
        }

        // 比例
        if (StringUtils.pathEquals(MemberCollectFeeTypeEnum.COLLECT_FEE_TYPE_R.getCode(), memberTransConfine.getCollectFeeType())) {
            return orderAmount.multiply(memberTransConfine.getRatioCollectFee());
        }

        // 单笔+比例
        if (StringUtils.pathEquals(MemberCollectFeeTypeEnum.COLLECT_FEE_TYPE_SR.getCode(), memberTransConfine.getCollectFeeType())) {
            return orderAmount.multiply(memberTransConfine.getRatioCollectFee()).add(memberTransConfine.getSingleCollectFee());
        }

        log.warn("获取提现手续费异常:{}", memberTransConfine.getCollectFeeType());

        // 手续费类型不匹配
        throw new OptimusException(RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "手续费类型不匹配");

    }

    @Override
    public MemberInfoDTO getMemberInfoByMemberId(String memberId) {

        log.info("根据会员编号查询会员信息,会员编号:{}", memberId);

        // 根据会员编号查询会员信息
        MemberInfoDO memberInfoDO = memberInfoDao.getMemberInfoByMemberId(memberId);

        // 会员信息DTO
        MemberInfoDTO memberInfo = new MemberInfoDTO();
        BeanUtils.copyProperties(memberInfoDO, memberInfo);

        log.info("会员信息:{}", memberInfo);

        return memberInfo;

    }

    @Override
    public MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId) {

        log.info("根据会员编号查询会员交易限制,会员编号:{}", memberId);

        // 查询会员交易限制
        MemberTransConfineDO memberTransConfineDO = memberTransConfineDao.getMemberTransConfineByMemberId(memberId);

        // 断言:会员交易限制
        AssertUtil.notEmpty(memberTransConfineDO, RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "会员交易限制不能为空");

        // 会员交易限制DTO
        MemberTransConfineDTO memberTransConfine = new MemberTransConfineDTO();
        BeanUtils.copyProperties(memberTransConfineDO, memberTransConfine);

        log.info("会员交易限制:{}", memberTransConfine);

        return memberTransConfine;

    }

    @Override
    @Cacheable(value = "systemMemberIdConfig", key = "#memberId", unless = "#result == null")
    public String getSystemMemberId(String memberId) {

        log.info("查询系统会员信息,会员编号:{}", memberId);

        // 根据会员编号查询会员信息
        List<MemberInfoChainResult> memberInfoChainResultList = listMemberInfoChains(memberId);
        AssertUtil.notEmpty(memberInfoChainResultList, RespCodeEnum.MEMBER_ERROR, "未查询到平台会员");

        // 排序:倒叙
        memberInfoChainResultList.sort(Comparator.comparing(MemberInfoChainResult::getLevel).reversed());

        // 取第一个
        MemberInfoChainResult memberInfoChainResult = memberInfoChainResultList.get(0);
        AssertUtil.notEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), memberInfoChainResult.getMemberType(), RespCodeEnum.MEMBER_ERROR, "未查询到平台会员");

        log.info("系统会员信息:{}", memberInfoChainResult);

        return memberInfoChainResult.getMemberId();
    }

    @Override
    @Cacheable(value = "memberChainConfig", key = "#memberId", unless = "#result == null")
    public List<MemberInfoChainResult> listMemberInfoChains(String memberId) {

        log.info("查询会员信息链,会员编号:{}", memberId);

        // 递归查询会员信息链
        List<MemberInfoChainResult> chainList = memberInfoDao.listMemberInfoChains(memberId);
        log.info("会员信息链:{}", chainList);

        if (CollectionUtils.isEmpty(chainList)) {
            return null;
        }

        return chainList;
    }

}
