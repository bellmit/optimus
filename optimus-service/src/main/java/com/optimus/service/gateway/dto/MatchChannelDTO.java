package com.optimus.service.gateway.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * MatchChannelDTO
 * 
 * @author sunxp
 */
@Data
public class MatchChannelDTO {

    /**
     * 会员编号
     */
    public String memberId;

    /**
     * 渠道编号
     */
    public String channelCode;

    /**
     * 订单金额
     */
    public BigDecimal orderAmount;

}
