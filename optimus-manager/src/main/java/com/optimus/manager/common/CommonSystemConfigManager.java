package com.optimus.manager.common;

/**
 * 系统配置Manager
 * 
 * @author sunxp
 */
public interface CommonSystemConfigManager {

    /**
     * 查询系统配置value
     * 
     * @param baseKey
     * @return
     */
    String getCommonSystemConfigForCache(String baseKey);

}
