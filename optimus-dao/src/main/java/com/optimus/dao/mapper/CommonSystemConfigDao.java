package com.optimus.dao.mapper;

import com.optimus.dao.domain.CommonSystemConfigDO;

import org.apache.ibatis.annotations.Param;

/**
 * CommonSystemConfigDao
 * 
 * @author sunxp
 */
public interface CommonSystemConfigDao {

    /**
     * getCommonSystemConfigByKey
     * 
     * @param key
     * @return
     */
    CommonSystemConfigDO getCommonSystemConfigByKey(@Param("key") String key);

}