package com.optimus.runner.config;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.model.resp.Resp;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * RespBodyConfig
 * 
 * @author sunxp
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class RespBodyConfig implements ResponseBodyAdvice<Object> {

    /**
     * supports
     */
    @Override
    public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
        return true;
    }

    /**
     * 前置写
     */
    @Override
    public Object beforeBodyWrite(Object arg0, MethodParameter arg1, MediaType arg2, Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {

        String className = arg1.getMethod().getDeclaringClass().getSimpleName();
        String methodName = arg1.getMethod().getName();

        if (arg0 instanceof String) {
            log.info("{}.{},响应:{}", className, methodName, arg0);
            return arg0;
        }

        if (arg0 instanceof Resp) {
            log.info("{}.{},响应:{}", className, methodName, arg0);
            return arg0;
        }

        Resp<Object> resp = new Resp<>(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMemo(), arg0);
        log.info("{}.{},响应:{}", className, methodName, resp);

        return resp;
    }

}
