package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * AccountInfoDO
 * 
 * @author sunxp
 */
@Data
public class AccountInfoDO implements Serializable {

    private static final long serialVersionUID = 3292437107022937393L;

    private Long id;

    private String memberId;

    private String accountId;

    private String accountType;

    private BigDecimal amount;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}