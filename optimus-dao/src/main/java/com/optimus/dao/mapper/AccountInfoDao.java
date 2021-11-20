package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.AccountInfoDO;

import org.apache.ibatis.annotations.Param;

/**
 * 账户信息Dao
 *
 * @author sunxp
 */
public interface AccountInfoDao {

    /**
     * 根据主键查询账户信息
     *
     * @param id
     * @return
     */
    AccountInfoDO getAccountInfoById(Long id);

    /**
     * 根据会员编号和账户类型查询账户信息
     *
     * @param memberId
     * @param accountType
     * @return OrderInfoDTO
     */
    AccountInfoDO getAccountInfoByMemberIdAndAccountType(@Param("memberId") String memberId, @Param("accountType") String accountType);

    /**
     * 根据会员编号集合查询账户信息
     * 
     * @param memberIdList
     * @return
     */
    List<AccountInfoDO> listAccountInfoByMemberIdLists(List<String> memberIdList);

    /**
     * 新增一条账户信息
     *
     * @param accountInfoDO
     * @return
     */
    int addAccountInfo(AccountInfoDO accountInfoDO);

    /**
     * 更新账户信息
     *
     * @param accountInfoDO
     * @return
     */
    int updateAccountInfo(AccountInfoDO accountInfoDO);

}