package com.optimus.dao.mapper;

import com.optimus.dao.domain.AccountInfoDO;

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