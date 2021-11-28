package com.optimus.runner.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.service.member.MemberService;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberStatusEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.model.req.Req;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemberService memberService;

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
    public HttpInputMessage beforeBodyRead(HttpInputMessage arg0, MethodParameter arg1, Type arg2, Class<? extends HttpMessageConverter<?>> arg3) throws IOException {

        byte[] bytes = arg0.getBody().readAllBytes();
        String body = new String(bytes);

        log.info("{}.{} param is {}", arg1.getMethod().getDeclaringClass().getSimpleName(), arg1.getMethod().getName(), body);

        handleForSign(body);

        return new HttpInputMessage() {

            @Override
            public HttpHeaders getHeaders() {
                return arg0.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(bytes);
            }

        };

    }

    /**
     * afterBodyRead
     */
    @Override
    public Object afterBodyRead(Object arg0, HttpInputMessage arg1, MethodParameter arg2, Type arg3, Class<? extends HttpMessageConverter<?>> arg4) {
        return arg0;
    }

    /**
     * handleEmptyBody
     */
    @Override
    public Object handleEmptyBody(Object arg0, HttpInputMessage arg1, MethodParameter arg2, Type arg3, Class<? extends HttpMessageConverter<?>> arg4) {
        return arg0;
    }

    /**
     * 
     * 验签处理
     * 
     * @param body
     */
    private void handleForSign(String body) {

        Req req = JacksonUtil.toBean(body, Req.class);
        String sign = req.getSign();
        String method = req.getMethod();
        Date timestamp = req.getTimestamp();
        String memberId = JacksonUtil.toTree(body).get("memberId").asText();

        AssertUtil.notEmpty(sign, RespCodeEnum.INVALID_PARAM, "签名串不能为空");
        AssertUtil.notEmpty(method, RespCodeEnum.INVALID_PARAM, "方法名不能为空");
        AssertUtil.notEmpty(timestamp, RespCodeEnum.INVALID_PARAM, "时间戳不能为空");
        AssertUtil.notEmpty(memberId, RespCodeEnum.INVALID_PARAM, "会员编号不能为空");

        Date currentDate = DateUtil.currentDate();
        if (currentDate.compareTo(timestamp) < 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳大于当前时间");
        }
        if (DateUtil.offsetForMinute(currentDate, -1).compareTo(timestamp) > 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳已过期");
        }

        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberIdForLimiter(memberId);
        AssertUtil.notEquals(MemberStatusEnum.MEMBER_STATUS_Y.getCode(), memberInfo.getMemberStatus(), RespCodeEnum.MEMBER_ERROR, "无效的会员");

        Map<String, Object> map = JacksonUtil.toBean(body, new TypeReference<Map<String, Object>>() {
        });

        String basisSign = SignUtil.sign(map, memberInfo.getMemberKey());
        AssertUtil.notEquals(sign, basisSign, RespCodeEnum.ERROR_SIGN, null);

    }

}
