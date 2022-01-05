package com.optimus.service.member;

import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.manager.member.dto.MemberTransConfineDTO;

/**
 * 会员Service
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
     * 根据memberId查询交易限制
     * 
     * @param memberId
     * @return
     */
    MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId);

    /**
     * 检查上下级关系
     * 
     * @param memberInfo
     * @param subDirectMemberId
     */
    void checkMemberLevel(MemberInfoDTO memberInfo, String subDirectMemberId);

}
