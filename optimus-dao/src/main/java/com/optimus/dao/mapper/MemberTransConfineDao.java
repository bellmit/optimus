package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberTransConfineDO;

/**
 * MemberTransConfineDao
 * 
 * @author sunxp
 */
public interface MemberTransConfineDao {

    /**
     * getMemberTransConfineById
     * 
     * @param id
     * @return
     */
    MemberTransConfineDO getMemberTransConfineById(Long id);

    /**
     * addMemberTransConfine
     * 
     * @param memberTransConfineDO
     * @return
     */
    int addMemberTransConfine(MemberTransConfineDO memberTransConfineDO);

    /**
     * updateMemberTransConfine
     * 
     * @param memberTransConfineDO
     * @return
     */
    int updateMemberTransConfine(MemberTransConfineDO memberTransConfineDO);

}