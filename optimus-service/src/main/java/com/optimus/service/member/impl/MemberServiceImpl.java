package com.optimus.service.member.impl;

import java.util.List;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.model.InviteChainModel;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * MemberServiceImpl
 * 
 * @author sunxp
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberInfoDao memberInfoDao;

    @Override
    public List<MemberInfoDO> getMemberInfoList(MemberInfoDO memberInfoDO) {

        log.info("getMemberInfoList memberInfoDO is {}", memberInfoDO);
        return memberInfoDao.getMemberInfoList(memberInfoDO);

    }

    @Override
    public List<InviteChainModel> buildInviteChain(String memberId) {
        return null;
    }

}
