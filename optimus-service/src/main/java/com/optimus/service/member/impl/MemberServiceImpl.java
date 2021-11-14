package com.optimus.service.member.impl;

import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.domain.MemberTransConfineDO;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.dao.mapper.MemberTransConfineDao;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.service.member.dto.MemberTransConfineDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberDeleteFlagEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 会员服务
 * 
 * @author sunxp
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberInfoDao memberInfoDao;

    @Resource
    private MemberTransConfineDao memberTransConfineDao;

    @Override
    public MemberInfoDTO getMemberInfoByMemberId(String memberId) {

        MemberInfoDO memberInfoDO = memberInfoDao.getMemberInfoByMemberId(memberId);

        AssertUtil.notEmpty(memberInfoDO, RespCodeEnum.MEMBER_NO, null);

        if (!StringUtils.pathEquals(memberInfoDO.getDeleteFlag(), MemberDeleteFlagEnum.DELETE_FLAG_ND.getCode())) {
            throw new OptimusException(RespCodeEnum.MEMBER_NO);
        }

        MemberInfoDTO memberInfo = new MemberInfoDTO();
        BeanUtils.copyProperties(memberInfoDO, memberInfo);

        return memberInfo;

    }

    @Override
    public MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId) {

        MemberTransConfineDO memberTransConfineDO = memberTransConfineDao.getMemberTransConfineByMemberId(memberId);

        AssertUtil.notEmpty(memberTransConfineDO, RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "会员交易限制为空");

        MemberTransConfineDTO memberTransConfine = new MemberTransConfineDTO();
        BeanUtils.copyProperties(memberTransConfineDO, memberTransConfine);

        return memberTransConfine;

    }

    @Override
    public void checkMemberLevel(MemberInfoDTO memberInfo, String subDirectMemberId) {

        MemberInfoDO subMemberInfo = memberInfoDao.getMemberInfoByMemberId(subDirectMemberId);

        AssertUtil.notEmpty(subMemberInfo, RespCodeEnum.MEMBER_NO, "下级会员不存在");

        // 验证上下级关系
        if (!StringUtils.pathEquals(memberInfo.getMemberId(), subMemberInfo.getSupDirectMemberId())) {
            throw new OptimusException(RespCodeEnum.MEMBER_LEVEL_ERROR);
        }

    }

    @Override
    public List<InviteChainDTO> buildInviteChain(String memberId) {
        return null;
    }

}
