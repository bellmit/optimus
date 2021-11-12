package com.optimus.manager.account.impl;

import java.util.List;

import com.optimus.manager.account.AccountInfoManager;
import com.optimus.manager.account.dto.DoTransDTO;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 账户信息Manager
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class AccountInfoManagerImpl implements AccountInfoManager {

    @Override
    public boolean doTrans(List<DoTransDTO> doTransList) {

        log.info("doTrans doTransList is {}", doTransList);

        return false;

    }

}
