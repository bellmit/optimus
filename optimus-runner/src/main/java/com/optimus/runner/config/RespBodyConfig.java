package com.optimus.runner.config;

import com.optimus.util.resp.Resp;

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
     * beforeBodyWrite
     */
    @Override
    public Object beforeBodyWrite(Object arg0, MethodParameter arg1, MediaType arg2,
            Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {

        String className = arg1.getMethod().getDeclaringClass().getSimpleName();
        String methodName = arg1.getMethod().getName();

        if (arg0 instanceof String) {
            log.info("{}.{} resp is {}", className, methodName, arg0);
            return arg0;
        }

        if (arg0 instanceof Resp) {
            log.info("{}.{} resp is {}", className, methodName, arg0);
            return arg0;
        }

        Resp<Object> resp = new Resp<>(arg0);
        log.info("{}.{} resp is {}", className, methodName, resp);

        return resp;
    }

    /**
     * supports
     */
    @Override
    public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
        return true;
    }

}
