package com.optimus.manager.member;

import java.math.BigDecimal;
import java.util.List;

import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.manager.member.dto.MemberTransConfineDTO;

/**
 * 用户manager
 *
 * @author hongp
 */
public interface MemberManager {

    /**
     * 获取手续费
     *
     * @param orderAmount
     *            订单金额
     * @param memberTransConfine
     *            交易限制
     * @return
     */
    BigDecimal getFee(BigDecimal orderAmount, MemberTransConfineDTO memberTransConfine);

    /**
     * 根据主键编号查询会员信息
     * 
     * 重要:调用方根据业务需求判断会员删除标识
     * 
     * @param memberId
     * @return
     */
    MemberInfoDTO getMemberInfoByMemberId(String memberId);

    /**
     * 根据memberId查询交易限制
     *
     * @param memberId
     * @return
     */
    MemberTransConfineDTO getMemberTransConfineByMemberId(String memberId);

    /**
     * 递归查询会员信息链
     * 
     * @param memberId
     * @return
     */
    List<MemberInfoChainResult> listMemberInfoChains(String memberId);

}
