package com.optimus.manager.account.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChangeAmountModel
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAmountModel implements Serializable {

    private static final long serialVersionUID = -6719708847330825714L;

    private String memberId;

    private String orderId;

    private String changeType;

    private BigDecimal amount;

    private String remark;

}
