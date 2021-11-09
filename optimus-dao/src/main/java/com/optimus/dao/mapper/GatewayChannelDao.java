package com.optimus.dao.mapper;

import com.optimus.dao.domain.GatewayChannelDO;

/**
 * GatewayChannelDao
 * 
 * @author sunxp
 */
public interface GatewayChannelDao {

    /**
     * getGatewayChannelById
     * 
     * @param id
     * @return
     */
    GatewayChannelDO getGatewayChannelById(Long id);

    /**
     * addGatewayChannel
     * 
     * @param gatewayChannelDO
     * @return
     */
    int addGatewayChannel(GatewayChannelDO gatewayChannelDO);

    /**
     * updateGatewayChannel
     * 
     * @param gatewayChannelDO
     * @return
     */
    int updateGatewayChannel(GatewayChannelDO gatewayChannelDO);

}