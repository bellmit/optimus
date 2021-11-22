package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.MemberAgentCodeRelDO;

import org.apache.ibatis.annotations.Param;

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
     * 根据会员编号和级别查询会员代理码商关系列表
     * 
     * @param memberId
     * @param memberLevel
     * @return
     */
    List<MemberAgentCodeRelDO> listMemberAgentCodeRelByMemberIdAndMemberLevels(@Param("memberId") String memberId, @Param("memberLevel") String memberLevel);

    /**
     * 根据会员编号和祖先会员编号查询会员渠道
     * 
     * @param memberId
     * @param ancestorMemberIdList
     * @return
     */
    List<MemberAgentCodeRelDO> listMemberAgentCodeRelByMemberIdAndAncestorMemberIdLists(@Param("memberId") String memberId, @Param("ancestorMemberIdList") List<String> ancestorMemberIdList);

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