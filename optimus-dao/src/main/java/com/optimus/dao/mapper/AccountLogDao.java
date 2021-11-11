package com.optimus.dao.mapper;

import com.optimus.dao.domain.AccountLogDO;

/**
 * 账户日志Dao
 * 
 * @author sunxp
 */
public interface AccountLogDao {

    /**
     * 根据主键查询账户日志
     * 
     * @param id
     * @return
     */
    AccountLogDO getAccountLogById(Long id);

    /**
     * 新增一条账户日志
     * 
     * @param accountLogDO
     * @return
     */
    int addAccountLog(AccountLogDO accountLogDO);

    /**
     * 更新账户日志记录
     * 
     * @param accountLogDO
     * @return
     */
    int updateAccountLog(AccountLogDO accountLogDO);

}