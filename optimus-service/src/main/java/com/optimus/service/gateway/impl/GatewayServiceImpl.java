package com.optimus.service.gateway.impl;

import java.math.BigDecimal;

import com.optimus.service.gateway.GatewayService;

import org.springframework.stereotype.Service;

/**
 * GatewayServiceImpl
 */
@Service
public class GatewayServiceImpl implements GatewayService {

    @Override
    public String matchChannel(String memberId, String channelCode, BigDecimal orderAmount) {
        return null;
    }

}
