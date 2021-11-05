package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberInfoDO;

/**
 * MemberInfoDao
 */
public interface MemberInfoDao {

    /**
     * getMemberInfoById
     * 
     * @param id
     * @return
     */
    MemberInfoDO getMemberInfoById(Long id);

    /**
     * addMemberInfo
     * 
     * @param memberInfoDO
     * @return
     */
    int addMemberInfo(MemberInfoDO memberInfoDO);

    /**
     * updateMemberInfo
     * 
     * @param memberInfoDO
     * @return
     */
    int updateMemberInfo(MemberInfoDO memberInfoDO);

}