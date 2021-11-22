package com.optimus.manager.member.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 会员渠道DTO
 * 
 * @author sunxp
 */
@Data
public class MemberChannelDTO implements Serializable {

    private static final long serialVersionUID = 4346737603163148270L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 网关子渠道编号
     */
    private String subChannelCode;

    /**
     * 费率
     */
    private BigDecimal rate;

}
