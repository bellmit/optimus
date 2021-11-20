package com.optimus.manager.account.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.AccountInfoDO;
import com.optimus.dao.mapper.AccountInfoDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.convert.AccountManagerConvert;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.account.validate.AccountManagerValidate;
import com.optimus.util.DateUtil;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 账户信息Manager实现
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class AccountManagerImpl implements AccountManager {

    @Resource
    private AccountInfoDao accountInfoDao;

    @Transactional(rollbackFor = { Exception.class, Error.class })
    @Override
    public boolean doTrans(List<DoTransDTO> doTransList) {

        log.info("doTrans doTransList is {}", doTransList);

        // 账户交易参数验证
        String validate = AccountManagerValidate.validateDoTrans(doTransList);
        if (StringUtils.hasLength(validate)) {
            log.info("doTrans validate is {}", validate);
            return false;
        }

        // 查询交易涉及账户
        List<String> memberIdList = doTransList.stream().map(DoTransDTO::getMemberId).distinct().collect(Collectors.toList());
        List<AccountInfoDO> accountInfoList = accountInfoDao.listAccountInfoByMemberIdLists(memberIdList);
        if (CollectionUtils.isEmpty(accountInfoList)) {
            log.info("doTrans accountInfoList is {}", accountInfoList);
            return false;
        }

        // 实际涉及账户
        accountInfoList = AccountManagerConvert.getAccountInfoDOList(accountInfoList, doTransList);
        if (CollectionUtils.isEmpty(accountInfoList)) {
            log.info("doTrans getAccountInfoDOList is {}", accountInfoList);
            return false;
        }

        // 更新账户
        int update = accountInfoDao.updateAccountInfoForTrans(accountInfoList, DateUtil.currentDate());
        if (update != accountInfoList.size()) {
            log.info("doTrans updateAccountInfoForTrans is {}, update is {}", accountInfoList.size(), update);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        // 查询账户交易后的数据
        List<Long> idList = accountInfoList.stream().map(AccountInfoDO::getId).collect(Collectors.toList());
        accountInfoList = accountInfoDao.listAccountInfoByIdLists(idList);

        // 记录账户日志

        return false;

    }

}
