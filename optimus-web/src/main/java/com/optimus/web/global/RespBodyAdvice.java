package com.optimus.web.global;

import com.optimus.web.global.resp.Resp;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * RespConfig
 */
@Configuration
@RestControllerAdvice
public class RespBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * beforeBodyWrite
     */
    @Override
    public Object beforeBodyWrite(Object arg0, MethodParameter arg1, MediaType arg2,
            Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {

        if (arg0 instanceof Resp) {
            return arg0;
        }

        return new Resp<>(arg0);
    }

    /**
     * supports
     */
    @Override
    public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
        return true;
    }

}
