package com.optimus.runner.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * DaoConfig
 * 
 * @author sunxp
 */
@Configuration
@EnableTransactionManagement
@MapperScan(value = "com.optimus.dao.mapper")
public class DaoConfig {

}
