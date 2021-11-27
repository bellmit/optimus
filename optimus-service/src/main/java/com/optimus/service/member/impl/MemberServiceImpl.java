package com.optimus.service.member.impl;

import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.service.member.MemberService;
import com.optimus.util.AssertUtil;
import com.optimus.util.annotation.OptimusRateLimiter;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberDeleteFlagEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员ServiceImpl
 *
 * @author sunxp
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberManager memberManager;

    @OptimusRateLimiter(permits = 1000D)
    @Override
    public MemberInfoDTO getMemberInfoByMemberIdForLimiter(String memberId) {
        // 获取号会员信息
        return memberManager.getMemberInfoByMemberId(memberId);
    }

    @Override
    public MemberInfoDTO getMemberInfoByMemberId(String memberId) {

        // 获取号会员信息
        MemberInfoDTO memberInfo = memberManager.getMemberInfoByMemberId(memberId);

        // 断言:不为空,未删除
        AssertUtil.notEmpty(memberInfo, RespCodeEnum.MEMBER_NO, null);
        AssertUtil.notEquals(MemberDeleteFlagEnum.DELETE_FLAG_ND.getCode(), memberInfo.getDeleteFlag(), RespCodeEnum.MEMBER_NO, null);

        return memberInfo;

    }

    @Override
    public MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId) {
        return memberManager.getMemberTransConfineByMemberId(memberId);
    }

    @Override
    public void checkMemberLevel(MemberInfoDTO memberInfo, String subDirectMemberId) {

        // 根据直接上级会员编号查询会员信息
        MemberInfoDTO subMemberInfo = memberManager.getMemberInfoByMemberId(subDirectMemberId);
        AssertUtil.notEmpty(subMemberInfo, RespCodeEnum.MEMBER_NO, "下级会员为空");

        // 验证上下级关系
        AssertUtil.notEquals(memberInfo.getMemberId(), subMemberInfo.getSupDirectMemberId(), RespCodeEnum.MEMBER_ERROR, "上下级关系异常");

    }

}
