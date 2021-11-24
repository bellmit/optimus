package com.optimus.manager.member.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberTransConfineDO;
import com.optimus.dao.mapper.MemberTransConfineDao;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberCollectFeeTypeEnum;
import com.optimus.util.constants.member.MemberWithdrawFeeSwitchEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用户managerImpl
 *
 * @author hongp
 */
@Component
public class MemberManagerImpl implements MemberManager {

    @Resource
    private MemberTransConfineDao memberTransConfineDao;

    @Override
    public BigDecimal getFee(BigDecimal orderAmount, MemberTransConfineDTO memberTransConfine) {

        // 验证会员交易限制
        if (!StringUtils.pathEquals(MemberWithdrawFeeSwitchEnum.WITHDRAW_FEE_SWITCH_Y.getCode(), memberTransConfine.getWithdrawFeeSwitch())) {
            return BigDecimal.ZERO;
        }

        // 断言:收取手续费类型
        AssertUtil.notEmpty(memberTransConfine.getCollectFeeType(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "手续费类型未配置");

        // 单笔
        if (StringUtils.pathEquals(MemberCollectFeeTypeEnum.COLLECT_FEE_TYPE_R.getCode(), memberTransConfine.getCollectFeeType())) {
            return orderAmount.multiply(memberTransConfine.getRatioCollectFee());
        }

        // 比例
        if (StringUtils.pathEquals(MemberCollectFeeTypeEnum.COLLECT_FEE_TYPE_S.getCode(), memberTransConfine.getCollectFeeType())) {
            return memberTransConfine.getSingleCollectFee();
        }

        // 单笔+比例
        if (StringUtils.pathEquals(MemberCollectFeeTypeEnum.COLLECT_FEE_TYPE_SR.getCode(), memberTransConfine.getCollectFeeType())) {
            return orderAmount.multiply(memberTransConfine.getRatioCollectFee()).add(memberTransConfine.getSingleCollectFee());
        }

        // 手续费类型不匹配
        throw new OptimusException(RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "手续费类型不匹配");

    }

    @Override
    public MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId) {

        // 查询会员交易限制
        MemberTransConfineDO memberTransConfineDO = memberTransConfineDao.getMemberTransConfineByMemberId(memberId);

        // 断言:会员交易限制
        AssertUtil.notEmpty(memberTransConfineDO, RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "会员交易限制为空");

        // 获取会员交易限制DTO
        MemberTransConfineDTO memberTransConfine = new MemberTransConfineDTO();
        BeanUtils.copyProperties(memberTransConfineDO, memberTransConfine);

        return memberTransConfine;

    }
}
