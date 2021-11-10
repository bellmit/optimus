package com.optimus.manager.account.impl;

import java.util.List;

import com.optimus.manager.account.AccountInfoManager;
import com.optimus.manager.account.dto.ChangeAmountDTO;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * AccountInfoManagerImpl
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class AccountInfoManagerImpl implements AccountInfoManager {

    @Override
    public boolean changeAmount(List<ChangeAmountDTO> changeAmountModelList) {

        log.info("changeAmount changeAmountModelList is {}", changeAmountModelList);

        return false;

    }

}
