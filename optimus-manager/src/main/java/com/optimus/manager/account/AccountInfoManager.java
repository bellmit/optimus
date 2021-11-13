package com.optimus.manager.account;

import java.util.List;

import com.optimus.manager.account.dto.DoTransDTO;

/**
 * 账户信息Manager
 * 
 * @author sunxp
 */
public interface AccountInfoManager {

    /**
     * 账户交易
     * 
     * @param changeAmountList
     * @return
     */
    boolean doTrans(List<DoTransDTO> changeAmountList);

}
