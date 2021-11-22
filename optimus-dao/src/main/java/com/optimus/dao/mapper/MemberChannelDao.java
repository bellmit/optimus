package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.MemberChannelDO;

import org.apache.ibatis.annotations.Param;

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
     * 根据会员编号列表和渠道编号查询会员渠道
     * 
     * @param memberIdList
     * @param channelCode
     * @return
     */
    List<MemberChannelDO> listMemberChannelByMemberIdListAndChannelCodes(@Param("memberIdList") List<String> memberIdList, @Param("channelCode") String channelCode);

    /**
     * 根据会员编号列表和渠道编号查询会员渠道
     * 
     * @param memberIdList
     * @param subChannelCode
     * @return
     */
    List<MemberChannelDO> listMemberChannelByMemberIdListAndSubChannelCodes(@Param("memberIdList") List<String> memberIdList, @Param("subChannelCode") String subChannelCode);

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