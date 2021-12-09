package com.optimus.service.common.impl;

import javax.annotation.Resource;

import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.stereotype.Component;

/**
 * 系统配置ServiceImpl
 * 
 * @author sunxp
 */
@Component
public class CommonSystemConfigServiceImpl implements CommonSystemConfigService {

    @Resource
    private CommonSystemConfigDao commonSystemConfigDao;

    @Override
    public String getCommonSystemConfigByBaseKey(String baseKey) {

        // 根据baseKey查询系统配置
        CommonSystemConfigDO commonSystemConfigDO = commonSystemConfigDao.getCommonSystemConfigByBaseKey(baseKey);
        AssertUtil.notEmpty(commonSystemConfigDO, RespCodeEnum.FAILE, "系统配置不能为空");

        // 系统配置值
        String value = commonSystemConfigDO.getValue();
        AssertUtil.notEmpty(value, RespCodeEnum.FAILE, "系统配置值不能为空");

        return value;

    }

}
