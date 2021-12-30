package com.optimus.dao.mapper;

import com.optimus.dao.domain.CommonSystemConfigDO;

import org.apache.ibatis.annotations.Param;

/**
 * 系统配置Dao
 * 
 * @author sunxp
 */
public interface CommonSystemConfigDao {

    /**
     * 根据type和baseKey查询系统配置
     * 
     * @param type
     * @param baseKey
     * @return
     */
    CommonSystemConfigDO getCommonSystemConfigByTypeAndBaseKey(@Param("type") String type, @Param("baseKey") String baseKey);

}