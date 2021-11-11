package com.optimus.dao.mapper;

import com.optimus.dao.domain.MemberChannelDO;

/**
 * 会员渠道Dao
 * 
 * @author sunxp
 */
public interface MemberChannelDao {

    /**
     * 根据主键查询会员渠道
     * 
     * @param id
     * @return
     */
    MemberChannelDO getMemberChannelById(Long id);

    /**
     * 新增一条会员渠道
     * 
     * @param memberChannelDO
     * @return
     */
    int addMemberChannel(MemberChannelDO memberChannelDO);

    /**
     * 更新会员渠道
     * 
     * @param memberChannelDO
     * @return
     */
    int updateMemberChannel(MemberChannelDO memberChannelDO);

}