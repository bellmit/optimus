package com.optimus.runner.config;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * ReqBodyConfig
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class ReqBodyConfig implements RequestBodyAdvice {

    /**
     * supports
     */
    @Override
    public boolean supports(MethodParameter arg0, Type arg1, Class<? extends HttpMessageConverter<?>> arg2) {
        return true;
    }

    /**
     * beforeBodyRead
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage arg0, MethodParameter arg1, Type arg2,
            Class<? extends HttpMessageConverter<?>> arg3) throws IOException {

        // 验签逻辑

        return arg0;

    }

    /**
     * afterBodyRead
     */
    @Override
    public Object afterBodyRead(Object arg0, HttpInputMessage arg1, MethodParameter arg2, Type arg3,
            Class<? extends HttpMessageConverter<?>> arg4) {

        Method method = arg2.getMethod();
        log.info("{}.{} is {}", method.getDeclaringClass().getSimpleName(), method.getName(), arg0);

        return arg0;
    }

    /**
     * handleEmptyBody
     */
    @Override
    public Object handleEmptyBody(Object arg0, HttpInputMessage arg1, MethodParameter arg2, Type arg3,
            Class<? extends HttpMessageConverter<?>> arg4) {
        return arg0;
    }

}
