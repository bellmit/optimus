package com.optimus.manager.account.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.AccountInfoDO;
import com.optimus.dao.domain.AccountLogDO;
import com.optimus.dao.mapper.AccountInfoDao;
import com.optimus.dao.mapper.AccountLogDao;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.convert.AccountManagerConvert;
import com.optimus.manager.account.dto.AccountInfoDTO;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.account.validate.AccountManagerValidate;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.extern.slf4j.Slf4j;

/**
 * 账户信息ManagerImpl
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class AccountManagerImpl implements AccountManager {

    @Resource
    private AccountInfoDao accountInfoDao;

    @Resource
    private AccountLogDao accountLogDao;

    @Override
    public AccountInfoDTO getAccountInfoByMemberIdAndAccountType(String memberId, String accountType) {

        // 根据会员编号和账户类型查询账户信息
        AccountInfoDO accountInfoDO = accountInfoDao.getAccountInfoByMemberIdAndAccountType(memberId, accountType);
        if (Objects.isNull(accountInfoDO)) {
            return null;
        }

        // 获取账户信息DTO
        AccountInfoDTO accountInfo = new AccountInfoDTO();
        BeanUtils.copyProperties(accountInfoDO, accountInfo);

        return accountInfo;
    }

    @Transactional(rollbackFor = { Exception.class, Error.class })
    @Override
    public boolean doTrans(List<DoTransDTO> doTransList) {

        try {

            log.info("doTrans doTransList is {}", doTransList);

            // 验证账户交易参数
            AccountManagerValidate.validateDoTrans(doTransList);

            // 构造账户信息
            List<AccountInfoDO> accountInfoList = buildAccountInfoList(doTransList);

            // 更新账户
            updateAccountInfoForTrans(accountInfoList);

            // 批量新增账户日志
            addBatchAccountLog(accountInfoList, doTransList);

        } catch (Exception e) {
            log.error("doTrans e is {}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;

    }

    /**
     * 构造账户信息
     * 
     * @param doTransList
     * @return
     */
    private List<AccountInfoDO> buildAccountInfoList(List<DoTransDTO> doTransList) {

        // 获得会员编号集合
        List<String> memberIdList = doTransList.stream().map(DoTransDTO::getMemberId).distinct().collect(Collectors.toList());

        // 查询会员所对应的所有账户信息
        List<AccountInfoDO> accountInfoList = accountInfoDao.listAccountInfoByMemberIdLists(memberIdList);

        // 断言:账户信息
        AssertUtil.notEmpty(accountInfoList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易查询账户信息为空");

        // 获得所涉及的账户信息
        accountInfoList = AccountManagerConvert.getAccountInfoDOList(accountInfoList, doTransList);
        log.info("buildAccountInfoList accountInfoList is {}", accountInfoList);

        // 断言:涉及账户信息
        AssertUtil.notEmpty(accountInfoList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易构造账户信息异常");

        return accountInfoList;

    }

    /**
     * 更新账户信息
     * 
     * @param accountInfoList
     */
    private void updateAccountInfoForTrans(List<AccountInfoDO> accountInfoList) {

        // 更新账户
        int update = accountInfoDao.updateAccountInfoForTrans(accountInfoList, DateUtil.currentDate());
        log.info("updateAccountInfoForTrans update is {}", update);

        // 验证更新结果
        if (update != accountInfoList.size()) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易更新账户异常");
        }

    }

    /**
     * 批量新增账户日志
     * 
     * @param accountInfoDOList
     * @param doTransList
     */
    private void addBatchAccountLog(List<AccountInfoDO> accountInfoDOList, List<DoTransDTO> doTransList) {

        // 获得账户主键集合
        List<Long> idList = accountInfoDOList.stream().map(AccountInfoDO::getId).distinct().collect(Collectors.toList());

        // 查询账户交易后的数据
        List<AccountInfoDO> accountInfoList = accountInfoDao.listAccountInfoByIdLists(idList);
        log.info("addBatchAccountLog accountInfoList is {}", accountInfoList);

        // 获得账户日志
        List<AccountLogDO> accountLogList = AccountManagerConvert.getAccountLogDOList(accountInfoList, doTransList);
        log.info("addBatchAccountLog accountLogList is {}", accountLogList);

        // 验证获得的账户日志
        AssertUtil.empty(accountLogList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易构造账户日志异常");

        // 记录账户日志
        accountLogDao.addBatchAccountLog(accountLogList);

    }

}
