package com.optimus.service.common.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

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

    @Resource
    private CommonSystemConfigDao commonSystemConfigDao;

    @Override
    public String getCommonSystemConfigByTypeAndBaseKey(String type, String baseKey) {

        log.info("根据键查询系统配置,类型:{};键:{}", type, baseKey);

        // 根据baseKey查询系统配置
        CommonSystemConfigDO commonSystemConfigDO = commonSystemConfigDao.getCommonSystemConfigByTypeAndBaseKey(type, baseKey);
        AssertUtil.notEmpty(commonSystemConfigDO, RespCodeEnum.FAILE, "系统配置不能为空");

        log.info("系统配置:{}", commonSystemConfigDO);

        // 系统配置值
        String value = commonSystemConfigDO.getValue();
        AssertUtil.notEmpty(value, RespCodeEnum.FAILE, "系统配置值不能为空");

        return value;

    }

}
