package com.optimus.web.collect.req;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 确认充值请求
 *
 * @author hongp
 */
@Data
public class ConfirmForRechargeReq extends Req {

    private static final long serialVersionUID = 8367300262598958078L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 下级会员编号
     */
    private String subMemberId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 会员信息
     */
    private MemberInfoDTO memberInfo;
}
