package com.optimus.web.collect.req;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值请求
 *
 * @author hongp
 */
@Data
public class RechargeReq extends Req {

    private static final long serialVersionUID = -7302042340018422438L;

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
