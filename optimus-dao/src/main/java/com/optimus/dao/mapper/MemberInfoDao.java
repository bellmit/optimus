package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.query.MemberInfoQuery;
import com.optimus.dao.result.MemberInfoForRecursionResult;

import org.apache.ibatis.annotations.Param;

/**
 * 会员信息Dao
 * 
 * @author sunxp
 */
public interface MemberInfoDao {

    /**
     * 根据主键查询会员信息
     * 
     * @param id
     * @return
     */
    MemberInfoDO getMemberInfoById(Long id);

    /**
     * 根据会员编号查询会员信息
     * 
     * @param memberId
     * @return
     */
    MemberInfoDO getMemberInfoByMemberId(String memberId);

    /**
     * 根据会员Query查询会员信息集合
     * 
     * @param query
     * @return
     */
    List<MemberInfoDO> listMemberInfoByMemberInfoQuerys(@Param("memberInfoQuery") MemberInfoQuery query);

    /**
     * 递归查询会员编号的父节点
     * 
     * @param memberId
     * @return
     */
    List<MemberInfoForRecursionResult> listMemberInfoForRecursions(String memberId);

    /**
     * 新增一条会员信息
     * 
     * @param memberInfoDO
     * @return
     */
    int addMemberInfo(MemberInfoDO memberInfoDO);

    /**
     * 更新会员信息
     * 
     * @param memberInfoDO
     * @return
     */
    int updateMemberInfo(MemberInfoDO memberInfoDO);

}