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
     * 重要:1.有限流需求时使用; 2.不处理已删除的会员
     * 
     * @param memberId
     * @return
     */
    MemberInfoDTO getMemberInfoByMemberIdForLimiter(String memberId);

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
