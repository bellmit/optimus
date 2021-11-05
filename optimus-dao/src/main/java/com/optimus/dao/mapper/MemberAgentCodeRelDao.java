package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberAgentCodeRelDO;

/**
 * MemberAgentCodeRelDao
 */
public interface MemberAgentCodeRelDao {

    /**
     * getMemberAgentCodeRelById
     * 
     * @param id
     * @return
     */
    MemberAgentCodeRelDO getMemberAgentCodeRelById(Long id);

    /**
     * addMemberAgentCodeRel
     * 
     * @param memberAgentCodeRelDO
     * @return
     */
    int addMemberAgentCodeRel(MemberAgentCodeRelDO memberAgentCodeRelDO);

    /**
     * updateMemberAgentCodeRel
     * 
     * @param memberAgentCodeRelDO
     * @return
     */
    int updateMemberAgentCodeRel(MemberAgentCodeRelDO memberAgentCodeRelDO);

}