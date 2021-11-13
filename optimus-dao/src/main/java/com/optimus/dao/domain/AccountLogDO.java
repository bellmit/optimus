package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.optimus.util.constants.account.AccountTransEnum;

import lombok.Data;

/**
 * 账户日志DO
 * 
 * @author sunxp
 */
@Data
public class AccountLogDO implements Serializable {

    private static final long serialVersionUID = 4008564990764227658L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账户编号
     */
    private String accountId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 流
     */
    private String flow;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 变更前金额
     */
    private BigDecimal beforeChangeAmount;

    /**
     * 变更后金额
     */
    private BigDecimal afterChangeAmount;

    /**
     * 交易类型
     * 
     * @see AccountTransEnum
     */
    private String changeType;

    /**
     * 备注
     */
    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}