package com.optimus.service.common;

/**
 * 系统配置Service
 * 
 * @author sunxp
 */
public interface CommonSystemConfigService {

    /**
     * 根据baseKey查询系统配置value
     * 
     * @param baseKey
     * @return
     */
    String getCommonSystemConfigByBaseKey(String baseKey);

}
