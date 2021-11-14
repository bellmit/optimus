package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberTransConfineDO;

/**
 * 会员交易限制Dao
 * 
 * @author sunxp
 */
public interface MemberTransConfineDao {

    /**
     * 根据主键查询会员交易限制
     * 
     * @param id
     * @return
     */
    MemberTransConfineDO getMemberTransConfineById(Long id);

    /**
     * 根据会员编号查询交易限制
     * 
     * @param memberId
     * @return
     */
    MemberTransConfineDO getMemberTransConfineByMemberId(String memberId);

    /**
     * 新增一条会员交易限制
     * 
     * @param memberTransConfineDO
     * @return
     */
    int addMemberTransConfine(MemberTransConfineDO memberTransConfineDO);

    /**
     * 更新会员交易限制
     * 
     * @param memberTransConfineDO
     * @return
     */
    int updateMemberTransConfine(MemberTransConfineDO memberTransConfineDO);

}