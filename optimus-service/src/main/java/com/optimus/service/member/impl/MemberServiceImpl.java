package com.optimus.service.member.impl;

import java.util.List;

import com.optimus.service.member.MemberService;
import com.optimus.service.member.model.InviteChainModel;

import org.springframework.stereotype.Service;

/**
 * MemberServiceImpl
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public List<InviteChainModel> buildInviteChain(String memberId) {
        return null;
    }

}
