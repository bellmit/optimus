package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.MemberInfoDO;

/**
 * MemberInfoDao
 * 
 * @author sunxp
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
     * getMemberInfoList
     * 
     * @param memberInfoDO
     * @return
     */
    List<MemberInfoDO> getMemberInfoList(MemberInfoDO memberInfoDO);

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