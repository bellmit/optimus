package com.optimus.service.common;

/**
 * 系统配置Service
 * 
 * @author sunxp
 */
public interface CommonSystemConfigService {

    /**
     * 根据type和baseKey查询系统配置value
     * 
     * @param type
     * @param baseKey
     * @return
     */
    String getCommonSystemConfigByTypeAndBaseKey(String type, String baseKey);

}
