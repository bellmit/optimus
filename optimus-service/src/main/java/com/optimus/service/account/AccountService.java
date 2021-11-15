package com.optimus.service.account;

import com.optimus.service.account.dto.AccountInfoDTO;

/**
 * 账户Service
 *
 * @author sunxp
 */
public interface AccountService {

    /**
     * 根据会员编号和账户类型查询账户信息
     *
     * @param memberId
     * @param accountType
     * @return OrderInfoDTO
     */
    AccountInfoDTO getAccountInfoByMemberIdAndAccountType(String memberId, String accountType);

}
