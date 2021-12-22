package com.optimus.manager.common.impl;

import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.manager.common.CommonSystemConfigManager;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统配置ManagerImpl
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class CommonSystemConfigManagerImpl implements CommonSystemConfigManager {

    @Resource
    private CommonSystemConfigDao commonSystemConfigDao;

    @Override
    @Cacheable(value = "systemConfig", key = "#baseKey", unless = "#result == null")
    public String getCommonSystemConfigByBaseKey(String baseKey) {

        String value = null;

        if (!StringUtils.hasLength(baseKey)) {
            log.warn("根据键查询系统配置:{}", baseKey);
            return value;
        }

        CommonSystemConfigDO commonSystemConfigDO = commonSystemConfigDao.getCommonSystemConfigByBaseKey(baseKey);
        log.info("根据键查询系统配置:{}", commonSystemConfigDO);

        if (Objects.isNull(commonSystemConfigDO)) {
            return value;
        }

        return commonSystemConfigDO.getValue();

    }

}
