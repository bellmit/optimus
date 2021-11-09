package com.optimus.dao.mapper;

import com.optimus.dao.domain.GatewaySubChannelDO;

/**
 * GatewaySubChannelDao
 * 
 * @author sunxp
 */
public interface GatewaySubChannelDao {

    /**
     * getGatewaySubChannelById
     * 
     * @param id
     * @return
     */
    GatewaySubChannelDO getGatewaySubChannelById(Long id);

    /**
     * addGatewaySubChannel
     * 
     * @param gatewaySubChannelDO
     * @return
     */
    int addGatewaySubChannel(GatewaySubChannelDO gatewaySubChannelDO);

    /**
     * updateGatewaySubChannel
     * 
     * @param gatewaySubChannelDO
     * @return
     */
    int updateGatewaySubChannel(GatewaySubChannelDO gatewaySubChannelDO);

}