package com.optimus.dao.mapper;

import com.optimus.dao.domain.GatewaySubChannelDO;

/**
 * 网关子渠道Dao
 * 
 * @author sunxp
 */
public interface GatewaySubChannelDao {

    /**
     * 根据主键查询网关子渠道
     * 
     * @param id
     * @return
     */
    GatewaySubChannelDO getGatewaySubChannelById(Long id);

    /**
     * 新增一条网关子渠道
     * 
     * @param gatewaySubChannelDO
     * @return
     */
    int addGatewaySubChannel(GatewaySubChannelDO gatewaySubChannelDO);

    /**
     * 更新网关子渠道
     * 
     * @param gatewaySubChannelDO
     * @return
     */
    int updateGatewaySubChannel(GatewaySubChannelDO gatewaySubChannelDO);

}