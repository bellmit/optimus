package com.optimus.service.common.impl;

import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.service.common.CommonSystemConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CommonSystemConfigServiceImpl
 * 
 * @author sunxp
 */
@Component
public class CommonSystemConfigServiceImpl implements CommonSystemConfigService {

    @Autowired
    private CommonSystemConfigManager commonSystemConfigManager;

    @Override
    public String getCommonSystemConfigByKey(String key) {
        return commonSystemConfigManager.getCommonSystemConfigByKey(key);
    }

}
