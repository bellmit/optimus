package com.optimus.runner.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.util.JsonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * ReqBodyConfig
 * 
 * @author sunxp
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

        Method method = arg1.getMethod();
        log.info("{}.{} is {}", method.getDeclaringClass().getSimpleName(), method.getName(), arg0);

        byte[] bytes = new byte[arg0.getBody().available()];
        arg0.getBody().read(bytes);

        String body = new String(bytes);

        JsonNode jsonNode = JsonUtil.toTree(body);
        Object signObject = jsonNode.get(SignUtil.SIGN).asText();
        if (Objects.isNull(signObject)) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "签名串为空");
        }

        Map<String, Object> map = JsonUtil.toBean(body, new TypeReference<Map<String, Object>>() {
        });

        String sign = SignUtil.sign(map, "SECRET_KEY");
        if (!signObject.toString().equals(sign)) {
            throw new OptimusException(RespCodeEnum.ERROR_SIGN);
        }

        InputStream input = new ByteArrayInputStream(bytes);
        return new HttpInputMessage() {

            @Override
            public HttpHeaders getHeaders() {
                return arg0.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return input;
            }
        };

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
