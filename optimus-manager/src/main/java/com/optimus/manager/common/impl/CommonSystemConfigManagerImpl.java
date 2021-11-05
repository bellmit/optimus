package com.optimus.manager.common.impl;

import java.util.Objects;

import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.manager.common.CommonSystemConfigManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * CommonSystemConfigManagerImpl
 */
@Component
@Slf4j
public class CommonSystemConfigManagerImpl implements CommonSystemConfigManager {

    @Autowired
    private CommonSystemConfigDao commonSystemConfigDao;

    @Override
    @Cacheable(value = "systemConfig", key = "#key", unless = "#result == null")
    public String getCommonSystemConfigByKey(String key) {

        String value = null;

        if (!StringUtils.hasLength(key)) {
            log.warn("key is null");
            return value;
        }

        CommonSystemConfigDO commonSystemConfigDO = commonSystemConfigDao.getCommonSystemConfigByKey(key);
        log.info("getCommonSystemConfigByKey is {}", commonSystemConfigDO);

        if (Objects.isNull(commonSystemConfigDO)) {
            return value;
        }

        value = commonSystemConfigDO.getValue();
        if (!StringUtils.hasLength(value)) {
            return value;
        }

        return value;
    }

}
