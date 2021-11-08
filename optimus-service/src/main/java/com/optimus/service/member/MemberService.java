package com.optimus.service.member;

import java.util.List;

import com.optimus.service.member.model.InviteChainModel;

/**
 * MemberService
 */
public interface MemberService {

    /**
     * 构建邀请链
     * 
     * @param memberId
     * @return
     */
    List<InviteChainModel> buildInviteChain(String memberId);

}
