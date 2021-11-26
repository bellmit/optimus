package com.optimus.manager.account;

import java.util.List;

import com.optimus.manager.account.dto.AccountInfoDTO;
import com.optimus.manager.account.dto.DoTransDTO;

/**
 * 账户Manager
 * 
 * @author sunxp
 */
public interface AccountManager {

    /**
     * 根据会员编号和账户类型查询账户信息
     *
     * @param memberId
     * @param accountType
     * @return OrderInfoDTO
     */
    AccountInfoDTO getAccountInfoByMemberIdAndAccountType(String memberId, String accountType);

    /**
     * 账户交易[事务]
     * 
     * @param doTransList
     * @return
     */
    boolean doTrans(List<DoTransDTO> doTransList);

}
