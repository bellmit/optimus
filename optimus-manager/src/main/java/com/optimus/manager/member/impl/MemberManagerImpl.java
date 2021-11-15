package com.optimus.manager.member.impl;

import com.optimus.dao.domain.MemberTransConfineDO;
import com.optimus.dao.mapper.MemberTransConfineDao;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberCollectFeeTypeEnum;
import com.optimus.util.constants.member.MemberWithdrawFeeSwitchEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;


/**
 * 用户manager实现
 *
 * @author hongp
 */
@Component
@Slf4j
public class MemberManagerImpl implements MemberManager {

    @Resource
    private MemberTransConfineDao memberTransConfineDao;

    @Override
    public BigDecimal getFee(BigDecimal orderAmount, MemberTransConfineDTO memberTransConfine) {

        // 验证会员交易限制
        if (!StringUtils.pathEquals(MemberWithdrawFeeSwitchEnum.WITHDRAW_FEE_SWITCH_Y.getCode(),
                memberTransConfine.getWithdrawFeeSwitch())) {
            return BigDecimal.ZERO;
        }

        MemberCollectFeeTypeEnum feeType = MemberCollectFeeTypeEnum.valueOf(memberTransConfine.getCollectFeeType());

        switch (feeType) {
            case COLLECT_FEE_TYPE_R:
                return orderAmount.multiply(memberTransConfine.getRatioCollectFee());
            case COLLECT_FEE_TYPE_S:
                return memberTransConfine.getSingleCollectFee();
            case COLLECT_FEE_TYPE_SR:
                return orderAmount.multiply(memberTransConfine.getRatioCollectFee()).add(memberTransConfine.getSingleCollectFee());
            default:
                return BigDecimal.ZERO;
        }
    }

    @Override
    public MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId) {

        MemberTransConfineDO memberTransConfineDO = memberTransConfineDao.getMemberTransConfineByMemberId(memberId);

        AssertUtil.notEmpty(memberTransConfineDO, RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "会员交易限制为空");

        MemberTransConfineDTO memberTransConfine = new MemberTransConfineDTO();
        BeanUtils.copyProperties(memberTransConfineDO, memberTransConfine);

        return memberTransConfine;

    }
}
