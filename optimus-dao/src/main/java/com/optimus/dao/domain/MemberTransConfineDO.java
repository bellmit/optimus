package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * MemberTransConfineDO
 */
@Data
@ToString
public class MemberTransConfineDO implements Serializable {

    private static final long serialVersionUID = -4035940679621994839L;

    private Long id;

    private String memberId;

    private String merchantOrderSwitch;

    private String withdrawFeeSwitch;

    private String freezeBalanceSwitch;

    private Short releaseFreezeBalanceAging;

    private BigDecimal singleMinAmount;

    private BigDecimal singleMaxAmount;

    private String collectFeeType;

    private BigDecimal singleCollectFee;

    private BigDecimal ratioCollectFee;

    private String collectFeeWay;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}