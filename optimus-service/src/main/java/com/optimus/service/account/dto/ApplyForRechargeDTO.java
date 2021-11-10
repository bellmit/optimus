package com.optimus.service.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 申请充值请求
 *
 * @author hongp
 */
@Data
public class ApplyForRechargeDTO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5062752258127246879L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;
}
