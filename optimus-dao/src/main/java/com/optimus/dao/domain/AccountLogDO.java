package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 账户日志DO
 * 
 * @author sunxp
 */
@Data
public class AccountLogDO implements Serializable {

    private static final long serialVersionUID = 4008564990764227658L;

    private Long id;

    private String accountId;

    private String orderId;

    private String flow;

    private BigDecimal amount;

    private BigDecimal beforeChangeAmount;

    private BigDecimal afterChangeAmount;

    private String changeType;

    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}