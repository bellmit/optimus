package com.optimus.manager.member;

import com.optimus.manager.member.dto.MemberTransConfineDTO;

import java.math.BigDecimal;

/**
 * 用户manager
 *
 * @author hongp
 */
public interface MemberManager {

    /**
     * 获取手续费
     *
     * @param orderAmount        订单金额
     * @param memberTransConfine 交易限制
     * @return
     */
    BigDecimal getFee(BigDecimal orderAmount, MemberTransConfineDTO memberTransConfine);

    /**
     * 根据memberId查询交易限制
     *
     * @param memberId
     * @return
     */
    MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId);

}
