package com.optimus.manager.account.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.optimus.dao.domain.AccountInfoDO;
import com.optimus.dao.domain.AccountLogDO;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.account.AccountFlowEnum;

import org.springframework.util.StringUtils;

/**
 * 账户ManagerConvert
 * 
 * @author sunxp
 */
public class AccountManagerConvert {

    /**
     * 获取账户信息
     * 
     * @param accountInfoDOList
     * @param doTransList
     * @return
     */
    public static List<AccountInfoDO> getAccountInfoDOList(List<AccountInfoDO> accountInfoDOList, List<DoTransDTO> doTransList) {

        // 账户信息Map
        Map<String, AccountInfoDO> accountInfoMap = buildAccountInfoMap(accountInfoDOList);

        // 账户信息List
        List<AccountInfoDO> accountInfoList = new ArrayList<>();
        AccountInfoDO accountInfo = null;

        // 遍历账户交易
        for (DoTransDTO item : doTransList) {

            String memberId = item.getMemberId();
            String changeType = item.getChangeType();
            String accountType = changeType.substring(0, changeType.length() - 1);
            String symbol = changeType.substring(changeType.length() - 1, changeType.length());
            String key = memberId + accountType;

            if (!accountInfoMap.containsKey(key)) {
                return null;
            }

            if (AccountFlowEnum.instanceOfSymbol(symbol) == null) {
                return null;
            }

            accountInfo = accountInfoMap.get(key);

            accountInfo.setAmount(item.getAmount());
            if (StringUtils.pathEquals(AccountFlowEnum.FLOW_S.getSymbol(), symbol)) {
                accountInfo.setAmount(item.getAmount().negate());
            }

            accountInfoList.add(accountInfo);

        }

        // 验证账户信息和账户交易必须一致
        if (accountInfoList.size() != doTransList.size()) {
            return null;
        }

        return accountInfoList;

    }

    /**
     * 获取账户日志
     * 
     * @param accountInfoList
     * @param doTransList
     * @return
     */
    public static List<AccountLogDO> getAccountLogDOList(List<AccountInfoDO> accountInfoList, List<DoTransDTO> doTransList) {

        // 账户日志Map
        Map<String, AccountInfoDO> accountInfoMap = buildAccountInfoMap(accountInfoList);

        // 账户日志List
        List<AccountLogDO> accountLogList = new ArrayList<>();
        AccountLogDO accountLog = null;

        // 遍历账户交易List
        for (DoTransDTO item : doTransList) {

            String memberId = item.getMemberId();
            String changeType = item.getChangeType();
            String accountType = changeType.substring(0, changeType.length() - 1);
            String symbol = changeType.substring(changeType.length() - 1, changeType.length());
            String key = memberId + accountType;

            if (AccountFlowEnum.instanceOfSymbol(symbol) == null) {
                return null;
            }

            AccountInfoDO accountInfo = accountInfoMap.get(key);
            BigDecimal beforeChangeAmount = accountInfo.getAmount().subtract(item.getAmount());
            if (StringUtils.pathEquals(AccountFlowEnum.FLOW_S.getSymbol(), symbol)) {
                beforeChangeAmount = accountInfo.getAmount().add(item.getAmount());
            }

            accountLog = new AccountLogDO();
            accountLog.setAccountId(accountInfo.getAccountId());
            accountLog.setOrderId(item.getOrderId());
            accountLog.setAmount(item.getAmount());
            accountLog.setBeforeChangeAmount(beforeChangeAmount);
            accountLog.setAfterChangeAmount(accountInfo.getAmount());
            accountLog.setChangeType(changeType);
            accountLog.setRemark(item.getRemark());
            accountLog.setCreateTime(DateUtil.currentDate());
            accountLog.setUpdateTime(DateUtil.currentDate());
            accountLog.setFlow(AccountFlowEnum.instanceOfSymbol(symbol).getCode());

            accountLogList.add(accountLog);

        }

        // 验证账户日志和账户交易必须一致
        if (accountLogList.size() != doTransList.size()) {
            return null;
        }

        return accountLogList;

    }

    /**
     * 构建账户信息
     * 
     * @param accountInfoList
     * @return
     */
    private static Map<String, AccountInfoDO> buildAccountInfoMap(List<AccountInfoDO> accountInfoList) {

        // 账户信息Map
        Map<String, AccountInfoDO> accountInfoMap = new HashMap<>(128);

        // 遍历账户信息List
        for (AccountInfoDO item : accountInfoList) {

            String memberId = item.getMemberId();
            String accountType = item.getAccountType();
            String key = memberId + accountType;

            if (accountInfoMap.containsKey(key)) {
                continue;
            }

            accountInfoMap.put(key, item);

        }

        return accountInfoMap;

    }

}
