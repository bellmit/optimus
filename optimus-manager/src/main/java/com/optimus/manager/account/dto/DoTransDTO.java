package com.optimus.manager.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.optimus.util.constants.AccountEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 做交易DTO
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoTransDTO implements Serializable {

    private static final long serialVersionUID = -6719708847330825714L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 变更类型
     * 
     * @see AccountEnum
     */
    private String changeType;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

}
