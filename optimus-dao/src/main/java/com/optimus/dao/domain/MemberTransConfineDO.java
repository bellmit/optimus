package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.optimus.util.constants.member.MemberCodeBalanceSwitchEnum;
import com.optimus.util.constants.member.MemberCollectFeeTypeEnum;
import com.optimus.util.constants.member.MemberCollectFeeWayEnum;
import com.optimus.util.constants.member.MemberFreezeBalanceSwitchEnum;
import com.optimus.util.constants.member.MemberMerchantOrderSwitchEnum;
import com.optimus.util.constants.member.MemberWithdrawFeeSwitchEnum;

import lombok.Data;

/**
 * 会员交易限制DO
 * 
 * @author sunxp
 */
@Data
public class MemberTransConfineDO implements Serializable {

    private static final long serialVersionUID = -4035940679621994839L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 商户下单开关
     * 
     * @see MemberMerchantOrderSwitchEnum
     */
    private String merchantOrderSwitch;

    /**
     * 提现手续费开关
     * 
     * @see MemberWithdrawFeeSwitchEnum
     */
    private String withdrawFeeSwitch;

    /**
     * 码商余额限制开关
     * 
     * @see MemberCodeBalanceSwitchEnum
     */
    private String codeBalanceSwitch;

    /**
     * 码商冻结余额开关
     * 
     * @see MemberFreezeBalanceSwitchEnum
     */
    private String freezeBalanceSwitch;

    /**
     * 释放冻结金额时效
     */
    private Short releaseFreezeBalanceAging;

    /**
     * 单笔最小金额
     */
    private BigDecimal singleMinAmount;

    /**
     * 单笔最大金额
     */
    private BigDecimal singleMaxAmount;

    /**
     * 收取手续费类型
     * 
     * @see MemberCollectFeeTypeEnum
     */
    private String collectFeeType;

    /**
     * 单笔收取手续费
     */
    private BigDecimal singleCollectFee;

    /**
     * 比例手续手续费
     */
    private BigDecimal ratioCollectFee;

    /**
     * 手续费收取方式
     * 
     * @see MemberCollectFeeWayEnum
     */
    private String collectFeeWay;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}