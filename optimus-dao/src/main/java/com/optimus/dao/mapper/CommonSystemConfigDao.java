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
     * 根据baseKey查询系统配置
     * 
     * @param baseKey
     * @return
     */
    CommonSystemConfigDO getCommonSystemConfigByBaseKey(@Param("baseKey") String baseKey);

}