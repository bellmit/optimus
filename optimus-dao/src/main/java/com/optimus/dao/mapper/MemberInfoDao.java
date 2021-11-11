package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.MemberInfoDO;

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
     * 查询会员信息列表
     * 
     * @param memberInfoDO
     * @return
     */
    List<MemberInfoDO> getMemberInfoList(MemberInfoDO memberInfoDO);

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