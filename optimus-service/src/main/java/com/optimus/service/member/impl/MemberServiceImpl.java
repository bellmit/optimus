package com.optimus.service.member.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.BeanUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * MemberServiceImpl
 * 
 * @author sunxp
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberInfoDao memberInfoDao;

    @Override
    public List<MemberInfoDTO> getMemberInfoList(MemberInfoDTO memberInfoDTO) {

        MemberInfoDO memberInfoDO = new MemberInfoDO();
        BeanUtils.copyProperties(memberInfoDTO, memberInfoDO);

        List<MemberInfoDO> memberInfoDOList = memberInfoDao.getMemberInfoList(memberInfoDO);

        List<MemberInfoDTO> memberInfoDTOList = new ArrayList<>();
        BeanUtil.copyProperties(memberInfoDOList, memberInfoDTOList, MemberInfoDTO.class);

        return memberInfoDTOList;

    }

    @Override
    public List<InviteChainDTO> buildInviteChain(String memberId) {
        return null;
    }

}
