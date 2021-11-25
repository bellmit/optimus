package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.query.MemberChannelQuery;

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
     * 根据会员编号集合查询会员渠道
     * 
     * @param memberIdList
     * @return
     */
    List<MemberChannelDO> listMemberChannelByMemberIdLists(@Param("memberIdList") List<String> memberIdList);

    /**
     * 根据会员渠道Query随机查询头N条会员渠道
     * 
     * @param query
     * @return
     */
    List<MemberChannelDO> listMemberChannelTopByMemberChannelQuerys(@Param("memberChannelQuery") MemberChannelQuery query);

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