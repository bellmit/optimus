package com.optimus.service.member;

import java.util.List;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.service.member.dto.InviteChainDTO;

/**
 * 会员服务
 * 
 * @author sunxp
 */
public interface MemberService {

    /**
     * 根据memberId查询用户信息
     * 
     * @param memberId
     * @return
     */
    MemberInfoDTO getMemberInfoByMemberId(String memberId);

    /**
     * 构建邀请链
     * 
     * @param memberId
     * @return
     */
    List<InviteChainDTO> buildInviteChain(String memberId);

}
