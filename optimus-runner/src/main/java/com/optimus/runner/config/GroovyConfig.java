package com.optimus.runner.config;

import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.util.constants.common.CommonSystemConfigEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import groovy.util.GroovyScriptEngine;

/**
 * GroovyConfig
 * 
 * @author sunxp
 */
@Configuration
public class GroovyConfig {

    @Autowired
    private CommonSystemConfigService commonSystemConfigService;

    @Bean("groovyScriptEngine")
    @Primary
    public GroovyScriptEngine groovyScriptEngine() throws Exception {

        // 查询系统配置
        String value = commonSystemConfigService.getCommonSystemConfigByBaseKey(CommonSystemConfigEnum.GROOVY_SCRIPT_PATH.getCode());

        // 实例化
        return new GroovyScriptEngine(value);
    }

}
