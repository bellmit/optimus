package com.optimus.service.member.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.service.member.MemberService;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberDeleteFlagEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员Service实现
 *
 * @author sunxp
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberInfoDao memberInfoDao;

    @Autowired
    private MemberManager memberManager;

    @Override
    public MemberInfoDTO getMemberInfoByMemberId(String memberId) {

        MemberInfoDO memberInfoDO = memberInfoDao.getMemberInfoByMemberId(memberId);

        AssertUtil.notEmpty(memberInfoDO, RespCodeEnum.MEMBER_NO, null);
        AssertUtil.notEquals(MemberDeleteFlagEnum.DELETE_FLAG_ND.getCode(), memberInfoDO.getDeleteFlag(), RespCodeEnum.MEMBER_NO, null);

        MemberInfoDTO memberInfo = new MemberInfoDTO();
        BeanUtils.copyProperties(memberInfoDO, memberInfo);

        return memberInfo;

    }

    @Override
    public MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId) {
        return memberManager.getMemberTransConfineByMemberId(memberId);
    }

    @Override
    public void checkMemberLevel(MemberInfoDTO memberInfo, String subDirectMemberId) {

        MemberInfoDO subMemberInfo = memberInfoDao.getMemberInfoByMemberId(subDirectMemberId);
        AssertUtil.notEmpty(subMemberInfo, RespCodeEnum.MEMBER_NO, "下级会员不存在");

        // 验证上下级关系
        AssertUtil.notEquals(memberInfo.getMemberId(), subMemberInfo.getSupDirectMemberId(), RespCodeEnum.MEMBER_ERROR, "上下级关系异常");

    }

}
