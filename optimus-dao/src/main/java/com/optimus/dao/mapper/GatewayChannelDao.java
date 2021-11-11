package com.optimus.dao.mapper;

import com.optimus.dao.domain.GatewayChannelDO;

/**
 * 网关渠道Dao
 * 
 * @author sunxp
 */
public interface GatewayChannelDao {

    /**
     * 根据主键查询网关渠道
     * 
     * @param id
     * @return
     */
    GatewayChannelDO getGatewayChannelById(Long id);

    /**
     * 新增一条网关渠道
     * 
     * @param gatewayChannelDO
     * @return
     */
    int addGatewayChannel(GatewayChannelDO gatewayChannelDO);

    /**
     * 更新网关渠道
     * 
     * @param gatewayChannelDO
     * @return
     */
    int updateGatewayChannel(GatewayChannelDO gatewayChannelDO);

}