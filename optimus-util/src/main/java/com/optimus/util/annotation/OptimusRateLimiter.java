package com.optimus.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流器注解
 * 
 * @author sunxp
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimusRateLimiter {

    /**
     * 每秒生成的令牌数
     * 
     * @return
     */
    double permits() default 10D;

    /**
     * 获取令牌超时时间(s)
     * 
     * @return
     */
    long timeout() default 0;

}
