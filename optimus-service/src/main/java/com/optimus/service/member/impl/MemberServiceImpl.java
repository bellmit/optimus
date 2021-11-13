package com.optimus.service.member.impl;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.member.dto.MemberInfoDTO;
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

    @Override
    public MemberInfoDTO getMemberInfoByMemberId(String memberId) {

        MemberInfoDO memberInfoDO = memberInfoDao.getMemberInfoByMemberId(memberId);

        if (Objects.isNull(memberInfoDO)) {
            throw new OptimusException(RespCodeEnum.MEMBER_NO);
        }
        if (!StringUtils.pathEquals(memberInfoDO.getDeleteFlag(), MemberDeleteFlagEnum.DELETE_FLAG_ND.getCode())) {
            throw new OptimusException(RespCodeEnum.MEMBER_NO);
        }

        MemberInfoDTO memberInfo = new MemberInfoDTO();
        BeanUtils.copyProperties(memberInfoDO, memberInfo);

        return memberInfo;

    }

    @Override
    public List<InviteChainDTO> buildInviteChain(String memberId) {
        return null;
    }

}
