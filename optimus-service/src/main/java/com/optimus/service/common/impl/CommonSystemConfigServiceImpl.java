package com.optimus.service.common.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统配置ServiceImpl
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class CommonSystemConfigServiceImpl implements CommonSystemConfigService {

    @Autowired
    private CommonSystemConfigManager commonSystemConfigManager;

    @Resource
    private CommonSystemConfigDao commonSystemConfigDao;

    @Override
    public String getCommonSystemConfigForCache(String baseKey) {
        return commonSystemConfigManager.getCommonSystemConfigForCache(baseKey);
    }

    @Override
    public String getCommonSystemConfigByTypeAndBaseKey(String type, String baseKey) {

        log.info("根据键查询系统配置,类型:{};键:{}", type, baseKey);

        // 根据baseKey查询系统配置
        CommonSystemConfigDO commonSystemConfig = commonSystemConfigDao.getCommonSystemConfigByTypeAndBaseKey(type, baseKey);
        AssertUtil.notEmpty(commonSystemConfig, RespCodeEnum.ERROR_CONFIG, "未配置系统配置");

        log.info("系统配置:{}", commonSystemConfig);

        // 系统配置值
        String value = commonSystemConfig.getValue();
        AssertUtil.notEmpty(value, RespCodeEnum.FAILE, "系统配置值不能为空");

        return value;

    }

}
