package com.optimus.service.account.impl;

import com.optimus.dao.domain.AccountInfoDO;
import com.optimus.dao.mapper.AccountInfoDao;
import com.optimus.service.account.AccountService;
import com.optimus.service.account.dto.AccountInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 账户Service实现
 *
 * @author sunxp
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountInfoDao accountInfoDao;

    @Override
    public AccountInfoDTO getAccountInfoByMemberIdAndAccountType(String memberId, String accountType) {

        AccountInfoDO accountInfoDO = accountInfoDao.getAccountInfoByMemberIdAndAccountType(memberId, accountType);

        if (Objects.isNull(accountInfoDO)) {
            return null;
        }

        AccountInfoDTO accountInfo = new AccountInfoDTO();
        BeanUtils.copyProperties(accountInfoDO, accountInfo);

        return accountInfo;
    }

}
