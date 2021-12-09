package com.optimus.runner.config;

import com.optimus.service.common.CommonSystemConfigService;

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

    private static final String GROOVY_SCRIPT_PATH_BASE_KEY = "GROOVY_SCRIPT_PATH";

    @Autowired
    private CommonSystemConfigService commonSystemConfigService;

    @Bean("groovyScriptEngine")
    @Primary
    public GroovyScriptEngine groovyScriptEngine() throws Exception {

        // 查询系统配置
        String value = commonSystemConfigService.getCommonSystemConfigByBaseKey(GROOVY_SCRIPT_PATH_BASE_KEY);

        // 实例化
        return new GroovyScriptEngine(value);
    }

}
