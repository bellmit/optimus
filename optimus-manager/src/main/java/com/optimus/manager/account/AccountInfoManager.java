package com.optimus.manager.account;

import java.util.List;

import com.optimus.manager.account.model.ChangeAmountModel;

/**
 * AccountInfoManager
 */
public interface AccountInfoManager {

    /**
     * 变更账户金额
     * 
     * @param changeAmountModelList
     * @return
     */
    boolean changeAmount(List<ChangeAmountModel> changeAmountModelList);

}
