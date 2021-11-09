package com.optimus.service.common.impl;

import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.service.common.CommonSystemConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CommonSystemConfigServiceImpl
 * 
 * 示例代码
 * 
 * @author sunxp
 */
@Service
public class CommonSystemConfigServiceImpl implements CommonSystemConfigService {

    @Autowired
    private CommonSystemConfigManager commonSystemConfigManager;

    @Override
    public String getCommonSystemConfigByBaseKey(String baseKey) {
        return commonSystemConfigManager.getCommonSystemConfigByBaseKey(baseKey);
    }

}
