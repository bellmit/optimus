package com.optimus.dao.mapper;

import java.util.List;

import com.optimus.dao.domain.GatewaySubChannelDO;

import org.apache.ibatis.annotations.Param;

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
     * 根据网关子渠道编号查询子渠道信息
     * 
     * @param channelCode
     * @return
     */
    GatewaySubChannelDO getGatewaySubChannelBySubChannelCode(String channelCode);

    /**
     * 根据网关父渠道编号和渠道状态渠道信息
     * 
     * @param parentChannelCode
     * @param channelStatus
     * @return
     */
    List<GatewaySubChannelDO> listGatewaySubChannelByParentChannelCodeAndChannelStatuss(@Param("parentChannelCode") String parentChannelCode, @Param("channelStatus") String channelStatus);

    /**
     * 根据网关子渠道编号集合和状态查询子渠道信息
     * 
     * @param channelCodeList
     * @param channelStatus
     * @return
     */
    List<GatewaySubChannelDO> listGatewaySubChannelBySubChannelCodeListAndChannelStatuss(@Param("channelCodeList") List<String> channelCodeList, @Param("channelStatus") String channelStatus);

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