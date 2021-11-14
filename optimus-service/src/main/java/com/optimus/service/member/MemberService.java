package com.optimus.service.member;

import java.util.List;

import com.optimus.service.member.dto.InviteChainDTO;
import com.optimus.service.member.dto.MemberInfoDTO;

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
     * 验证上下级关系
     * 
     * @param memberInfo
     * @param subDirectMemberId
     */
    void checkMemberLevel(MemberInfoDTO memberInfo, String subDirectMemberId);

    /**
     * 构建邀请链
     * 
     * @param memberId
     * @return
     */
    List<InviteChainDTO> buildInviteChain(String memberId);

}
