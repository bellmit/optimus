package com.optimus.web.collect.req;

import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 划账请求
 *
 * @author hongp
 */
@Data
public class TransferReq extends Req {

    private static final long serialVersionUID = -6820823098344909502L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 调用方订单号
     */
    private String callerOrderId;

    /**
     * 划账类型 type[类型：余额-预付款/预付款-余额]
     */
    private String transferType;

    /**
     * 会员信息
     */
    private MemberInfoDTO memberInfo;
}
