package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberChannelDO;

/**
 * MemberChannelDao
 */
public interface MemberChannelDao {

    /**
     * getMemberChannelById
     * 
     * @param id
     * @return
     */
    MemberChannelDO getMemberChannelById(Long id);

    /**
     * addMemberChannel
     * 
     * @param memberChannelDO
     * @return
     */
    int addMemberChannel(MemberChannelDO memberChannelDO);

    /**
     * updateMemberChannel
     * 
     * @param memberChannelDO
     * @return
     */
    int updateMemberChannel(MemberChannelDO memberChannelDO);

}