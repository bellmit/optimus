package com.optimus.service.member;

import java.util.List;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.service.member.model.InviteChainModel;

/**
 * MemberService
 */
public interface MemberService {

    /**
     * 查询用户信息列表
     * 
     * @param memberInfoDO
     * @return
     */
    List<MemberInfoDO> getMemberInfoList(MemberInfoDO memberInfoDO);

    /**
     * 构建邀请链
     * 
     * @param memberId
     * @return
     */
    List<InviteChainModel> buildInviteChain(String memberId);

}
