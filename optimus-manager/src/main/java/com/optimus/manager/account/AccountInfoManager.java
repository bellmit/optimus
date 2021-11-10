package com.optimus.manager.account;

import java.util.List;

import com.optimus.manager.account.dto.ChangeAmountDTO;

/**
 * AccountInfoManager
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
    boolean changeAmount(List<ChangeAmountDTO> changeAmountDTOList);

}
