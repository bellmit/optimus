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
import com.optimus.util.model.exception.OptimusException;

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

        log.info("根据会员编号和账户类型查询账户信息,会员编号:{},账户类型:{}", memberId, accountType);

        // 根据会员编号和账户类型查询账户信息
        AccountInfoDO accountInfoDO = accountInfoDao.getAccountInfoByMemberIdAndAccountType(memberId, accountType);
        if (Objects.isNull(accountInfoDO)) {
            log.warn("账户信息:{}", accountInfoDO);
            return null;
        }

        // 账户信息DTO
        AccountInfoDTO accountInfo = new AccountInfoDTO();
        BeanUtils.copyProperties(accountInfoDO, accountInfo);

        log.warn("账户信息:{}", accountInfo);

        return accountInfo;
    }

    @Transactional(rollbackFor = { Exception.class, Error.class })
    @Override
    public boolean doTrans(List<DoTransDTO> doTransList) {

        log.info("账户交易入参:{}", doTransList);

        try {

            // 验证账户交易参数
            AccountManagerValidate.validateDoTrans(doTransList);

            // 构建账户信息
            List<AccountInfoDO> accountInfoList = buildAccountInfoList(doTransList);

            // 更新账户信息
            updateAccountInfoForTrans(accountInfoList);

            // 批量新增账户日志
            addBatchAccountLog(accountInfoList, doTransList);

        } catch (OptimusException e) {
            log.warn("账户交易异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        } catch (Exception e) {
            log.error("账户交易异常:", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;

    }

    /**
     * 构建账户信息
     * 
     * @param doTransList
     * @return
     */
    private List<AccountInfoDO> buildAccountInfoList(List<DoTransDTO> doTransList) {

        // 获取会员编号List
        List<String> memberIdList = doTransList.stream().map(DoTransDTO::getMemberId).distinct().collect(Collectors.toList());

        // 查询会员所对应的所有账户信息
        List<AccountInfoDO> accountInfoList = accountInfoDao.listAccountInfoByMemberIdLists(memberIdList);

        // 断言:账户信息
        AssertUtil.notEmpty(accountInfoList, RespCodeEnum.ACCOUNT_ERROR, "账户信息不能为空");

        // 获取涉及的账户信息
        accountInfoList = AccountManagerConvert.getAccountInfoDOList(accountInfoList, doTransList);

        // 断言:涉及账户信息
        AssertUtil.notEmpty(accountInfoList, RespCodeEnum.ACCOUNT_ERROR, "账户信息涉及的账户不能为空");

        log.info("账户交易涉及的账户信息:{}", accountInfoList);

        return accountInfoList;

    }

    /**
     * 更新账户信息
     * 
     * @param accountInfoList
     */
    private void updateAccountInfoForTrans(List<AccountInfoDO> accountInfoList) {

        // 更新账户信息
        int update = accountInfoDao.updateAccountInfoForTrans(accountInfoList, DateUtil.currentDate());

        // 验证更新结果
        if (update != accountInfoList.size()) {
            log.warn("账户交易更新账户信息结果:{},期望结果:{}", update, accountInfoList.size());
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易更新账户信息异常");
        }

    }

    /**
     * 批量新增账户日志
     * 
     * @param accountInfoDOList
     * @param doTransList
     */
    private void addBatchAccountLog(List<AccountInfoDO> accountInfoDOList, List<DoTransDTO> doTransList) {

        // 获取账户主键List
        List<Long> idList = accountInfoDOList.stream().map(AccountInfoDO::getId).distinct().collect(Collectors.toList());

        // 查询账户交易后的数据
        List<AccountInfoDO> accountInfoList = accountInfoDao.listAccountInfoByIdLists(idList);

        // 获取账户日志
        List<AccountLogDO> accountLogList = AccountManagerConvert.getAccountLogDOList(accountInfoList, doTransList);

        // 验证获取的账户日志
        AssertUtil.notEmpty(accountLogList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易账户日志对象不能为空");

        // 记录账户日志
        accountLogDao.addBatchAccountLog(accountLogList);

    }

}
