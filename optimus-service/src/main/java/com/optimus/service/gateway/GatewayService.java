package com.optimus.service.gateway;

import java.math.BigDecimal;

/**
 * GatewayService
 * 
 * @author sunxp
 */
public interface GatewayService {

    /**
     * 匹配渠道
     * 
     * @param memberId    订单属主会员编号
     * @param channelCode 主渠道编号
     * @param orderAmount 订单金额
     * @return 子渠道编号
     */
    String matchChannel(String memberId, String channelCode, BigDecimal orderAmount);

}
