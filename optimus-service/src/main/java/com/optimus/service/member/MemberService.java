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
     * 查询用户信息列表
     * 
     * @param memberInfoDTO
     * @return
     */
    List<MemberInfoDTO> getMemberInfoList(MemberInfoDTO memberInfoDTO);

    /**
     * 构建邀请链
     * 
     * @param memberId
     * @return
     */
    List<InviteChainDTO> buildInviteChain(String memberId);

}
