package com.optimus.service.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 申请提现请求
 *
 * @author hongp
 */
@Data
public class ApplyForWithdrawDTO implements Serializable {

    private static final long serialVersionUID = 1523685270429362097L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;
}
