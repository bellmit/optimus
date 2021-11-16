package com.optimus.manager.account.impl;

import java.util.List;

import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 账户信息Manager实现
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class AccountManagerImpl implements AccountManager {

    @Override
    public boolean doTrans(List<DoTransDTO> doTransList) {

        log.info("doTrans doTransList is {}", doTransList);

        return false;

    }

}
