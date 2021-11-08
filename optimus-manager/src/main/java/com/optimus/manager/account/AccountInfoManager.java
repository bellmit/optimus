package com.optimus.manager.account;

import java.util.List;

import com.optimus.manager.account.model.ChangeAmountModel;

/**
 * AccountInfoManager
 */
public interface AccountInfoManager {

    /**
     * changeAmount
     * 
     * @param changeAmountModelList
     * @return
     */
    boolean changeAmount(List<ChangeAmountModel> changeAmountModelList);

}
