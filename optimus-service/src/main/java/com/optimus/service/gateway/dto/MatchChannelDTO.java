package com.optimus.service.gateway.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 网关服务DTO
 * 
 * @author sunxp
 */
@Data
public class MatchChannelDTO implements Serializable {

    private static final long serialVersionUID = -4245233461399358173L;

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
