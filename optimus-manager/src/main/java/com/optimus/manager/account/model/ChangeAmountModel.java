package com.optimus.manager.account.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

/**
 * ChangeAmountModel
 */
@Data
@ToString
public class ChangeAmountModel implements Serializable {

    private static final long serialVersionUID = -6719708847330825714L;

    private String memberId;

    private String orderId;

    private String code;

    private BigDecimal amount;

    private String remark;

}
