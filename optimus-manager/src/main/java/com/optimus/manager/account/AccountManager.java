package com.optimus.manager.account;

import java.util.List;

import com.optimus.manager.account.dto.DoTransDTO;

/**
 * 账户Manager
 * 
 * @author sunxp
 */
public interface AccountManager {

    /**
     * 账户交易
     * 
     * @param doTransList
     * @return
     */
    boolean doTrans(List<DoTransDTO> doTransList);

}
