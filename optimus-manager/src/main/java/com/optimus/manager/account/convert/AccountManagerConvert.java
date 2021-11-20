package com.optimus.manager.account.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.optimus.dao.domain.AccountInfoDO;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.util.constants.account.AccountFlowEnum;

import org.springframework.util.StringUtils;

/**
 * 账户Manager转换器
 * 
 * @author sunxp
 */
public class AccountManagerConvert {

    /**
     * 获取账户信息集合
     * 
     * @param list
     * @param doTransList
     * @return
     */
    public static List<AccountInfoDO> getAccountInfoDOList(List<AccountInfoDO> list, List<DoTransDTO> doTransList) {

        Map<String, AccountInfoDO> accountInfoMap = new HashMap<>(128);

        for (AccountInfoDO item : list) {

            String memberId = item.getMemberId();
            String accountType = item.getAccountType();
            String key = memberId + accountType;

            if (accountInfoMap.containsKey(key)) {
                continue;
            }

            accountInfoMap.put(key, item);

        }

        List<AccountInfoDO> accountInfoList = new ArrayList<>();
        AccountInfoDO accountInfo = null;

        for (DoTransDTO item : doTransList) {

            String memberId = item.getMemberId();
            String changeType = item.getChangeType();
            String accountType = changeType.substring(0, changeType.length() - 1);
            String symbol = changeType.substring(changeType.length() - 1, changeType.length());
            String key = memberId + accountType;

            if (!accountInfoMap.containsKey(key)) {
                continue;
            }

            accountInfo = accountInfoMap.get(key);
            if (StringUtils.pathEquals(AccountFlowEnum.ACCOUNT_FLOW_I.getCode(), symbol)) {
                accountInfo.setAmount(item.getAmount());
            }
            if (StringUtils.pathEquals(AccountFlowEnum.ACCOUNT_FLOW_S.getCode(), symbol)) {
                accountInfo.setAmount(item.getAmount().negate());
            }

            accountInfoList.add(accountInfo);

        }

        if (accountInfoList.size() != doTransList.size()) {
            return null;
        }

        return accountInfoList;

    }

}
