package com.optimus.web.collect.req;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现请求
 *
 * @author hongp
 */
@Data
public class WithdrawReq extends Req {

    private static final long serialVersionUID = -2935475925686536698L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 直接下级会员编号
     */
    private String subDirectMemberId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 调用方订单号
     */
    private String callerOrderId;

    /**
     * 会员信息
     */
    private MemberInfoDTO memberInfo;
}
