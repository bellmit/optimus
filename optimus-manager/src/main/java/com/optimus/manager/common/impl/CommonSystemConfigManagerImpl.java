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

import javax.annotation.Resource;

/**
 * 系统配置Manager实现
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
            log.warn("baseKey is null");
            return value;
        }

        CommonSystemConfigDO commonSystemConfigDO = commonSystemConfigDao.getCommonSystemConfigByBaseKey(baseKey);
        log.info("getCommonSystemConfigByBaseKey is {}", commonSystemConfigDO);

        if (Objects.isNull(commonSystemConfigDO)) {
            return value;
        }

        return commonSystemConfigDO.getValue();

    }

}
