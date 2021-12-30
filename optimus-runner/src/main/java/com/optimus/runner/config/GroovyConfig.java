package com.optimus.runner.config;

import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.common.CommonSystemConfigTypeEnum;

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

        // 类型及键
        String type = CommonSystemConfigTypeEnum.TYPE_S.getCode();
        String baseKey = CommonSystemConfigBaseKeyEnum.GROOVY_SCRIPT_PATH.getCode();

        // 查询系统配置
        String value = commonSystemConfigService.getCommonSystemConfigByTypeAndBaseKey(type, baseKey);

        // 实例化
        return new GroovyScriptEngine(value);
    }

}
