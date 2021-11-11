package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberAgentCodeRelDO;

/**
 * 会员代理码商关系Dao
 * 
 * @author sunxp
 */
public interface MemberAgentCodeRelDao {

    /**
     * 根据主键查询会员代理码商关系
     * 
     * @param id
     * @return
     */
    MemberAgentCodeRelDO getMemberAgentCodeRelById(Long id);

    /**
     * 新增一条会员代理码商关系
     * 
     * @param memberAgentCodeRelDO
     * @return
     */
    int addMemberAgentCodeRel(MemberAgentCodeRelDO memberAgentCodeRelDO);

    /**
     * 更新会员代理码商关系
     * 
     * @param memberAgentCodeRelDO
     * @return
     */
    int updateMemberAgentCodeRel(MemberAgentCodeRelDO memberAgentCodeRelDO);

}