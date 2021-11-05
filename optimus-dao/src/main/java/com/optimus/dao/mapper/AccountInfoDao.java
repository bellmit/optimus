package com.optimus.dao.mapper;

import com.optimus.dao.domain.AccountInfoDO;

/**
 * AccountInfoDao
 */
public interface AccountInfoDao {

    /**
     * getAccountInfoById
     * 
     * @param id
     * @return
     */
    AccountInfoDO getAccountInfoById(Long id);

    /**
     * addAccountInfo
     * 
     * @param accountInfoDO
     * @return
     */
    int addAccountInfo(AccountInfoDO accountInfoDO);

    /**
     * updateAccountInfo
     * 
     * @param accountInfoDO
     * @return
     */
    int updateAccountInfo(AccountInfoDO accountInfoDO);

}