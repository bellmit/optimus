package com.optimus.dao.mapper;

import com.optimus.dao.domain.AccountLogDO;

/**
 * AccountLogDao
 */
public interface AccountLogDao {

    /**
     * getAccountLogById
     * 
     * @param id
     * @return
     */
    AccountLogDO getAccountLogById(Long id);

    /**
     * addAccountLog
     * 
     * @param accountLogDO
     * @return
     */
    int addAccountLog(AccountLogDO accountLogDO);

    /**
     * updateAccountLog
     * 
     * @param accountLogDO
     * @return
     */
    int updateAccountLog(AccountLogDO accountLogDO);

}