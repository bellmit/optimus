package com.optimus.manager.gateway.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 匹配渠道DTO
 * 
 * @author sunxp
 */
@Data
public class MatchChannelDTO implements Serializable {

    private static final long serialVersionUID = -2677403069431360571L;

    /**
     * 码商会员编号
     */
    private String codeMemberId;

    /**
     * 网关子渠道DTO
     */
    private GatewaySubChannelDTO gatewaySubChannel;

}
