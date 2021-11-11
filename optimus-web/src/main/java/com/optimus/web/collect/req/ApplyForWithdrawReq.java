package com.optimus.web.collect.req;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 申请提现请求
 *
 * @author hongp
 */
@Data
public class ApplyForWithdrawReq extends Req {

    private static final long serialVersionUID = -6551652780157961227L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 会员信息
     */
    private MemberInfoDTO memberInfo;

}
