package com.optimus.dao.mapper;

import com.optimus.dao.domain.CommonSystemConfigDO;

import org.apache.ibatis.annotations.Param;

/**
 * CommonSystemConfigDao
 */
public interface CommonSystemConfigDao {

    /**
     * getCommonSystemConfigByBaseKey
     * 
     * @param baseKey
     * @return
     */
    CommonSystemConfigDO getCommonSystemConfigByBaseKey(@Param("baseKey") String baseKey);

}