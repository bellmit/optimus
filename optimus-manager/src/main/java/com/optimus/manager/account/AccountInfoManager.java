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
     * 变更账户金额
     * 
     * @param changeAmountDTOList
     * @return
     */
    boolean doTrans(List<DoTransDTO> changeAmountDTOList);

}
