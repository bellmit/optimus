package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.optimus.util.constants.account.AccountTypeEnum;

import lombok.Data;

/**
 * 账户信息DO
 * 
 * @author sunxp
 */
@Data
public class AccountInfoDO implements Serializable {

    private static final long serialVersionUID = 3292437107022937393L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 账户编号
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

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}