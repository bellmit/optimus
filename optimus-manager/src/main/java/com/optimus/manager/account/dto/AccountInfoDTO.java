package com.optimus.manager.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.account.AccountTypeEnum;

import lombok.Data;

/**
 * 账户信息DTO
 *
 * @author hongp
 */
@Data
public class AccountInfoDTO implements Serializable {

    private static final long serialVersionUID = 8500512400560688302L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 账户类型
     * 
     * @see AccountTypeEnum
     */
    private String accountType;

    /**
     * 账户金额
     */
    private BigDecimal amount;
}
