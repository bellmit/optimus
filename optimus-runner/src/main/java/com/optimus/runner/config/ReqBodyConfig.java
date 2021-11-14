package com.optimus.runner.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.JsonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberStatusEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.req.Req;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
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

    /** 特例方法集合 */
    private static final List<String> METHOD_NAME_LIST = Arrays.asList("channelCallback");

    /** 特例方法-渠道回调平台 */
    private static final String CHANNEL_CALLBACK = "channelCallback";

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
    public HttpInputMessage beforeBodyRead(HttpInputMessage arg0, MethodParameter arg1, Type arg2,
            Class<? extends HttpMessageConverter<?>> arg3) throws IOException {

        Method method = arg1.getMethod();
        InputStream inputStream = arg0.getBody();

        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String body = new String(bytes);

        log.info("{}.{} param is {}", method.getDeclaringClass().getSimpleName(), method.getName(), body);

        JsonNode jsonNode = JsonUtil.toTree(body);

        handleForBase(body, jsonNode);
        handleForChannelCallback(body, jsonNode);

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
    public Object afterBodyRead(Object arg0, HttpInputMessage arg1, MethodParameter arg2, Type arg3,
            Class<? extends HttpMessageConverter<?>> arg4) {
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

    /**
     * 
     * 基础请求处理
     * 
     * @param body
     * @param jsonNode
     */
    private void handleForBase(String body, JsonNode jsonNode) {

        Req req = JsonUtil.toBean(body, Req.class);
        String sign = req.getSign();
        String method = req.getMethod();
        Date timestamp = req.getTimestamp();
        String memberId = jsonNode.get("memberId").asText();
        Date currentDate = DateUtil.currentDate();

        if (METHOD_NAME_LIST.contains(method)) {
            return;
        }

        AssertUtil.notEmpty(sign, RespCodeEnum.INVALID_PARAM, "签名串不能为空");
        AssertUtil.notEmpty(method, RespCodeEnum.INVALID_PARAM, "方法名不能为空");
        AssertUtil.notEmpty(timestamp, RespCodeEnum.INVALID_PARAM, "时间戳不能为空");
        AssertUtil.notEmpty(memberId, RespCodeEnum.INVALID_PARAM, "会员编号不能为空");

        if (currentDate.compareTo(timestamp) < 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳大于当前时间");
        }
        if (DateUtil.offsetForMinute(currentDate, -1).compareTo(timestamp) > 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳已过期");
        }

        MemberInfoDTO memberInfo = memberService.getMemberInfoByMemberId(memberId);
        if (!StringUtils.pathEquals(memberInfo.getMemberStatus(), MemberStatusEnum.MEMBER_STATUS_Y.getCode())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员无效");
        }

        Map<String, String> map = JsonUtil.toBean(body, new TypeReference<Map<String, String>>() {
        });

        String basisSign = SignUtil.sign(map, memberInfo.getMemberKey());
        if (!StringUtils.pathEquals(sign, basisSign)) {
            throw new OptimusException(RespCodeEnum.ERROR_SIGN);
        }

    }

    /**
     * 网关渠道回调平台请求处理
     * 
     * @param body
     * @param jsonNode
     */
    private void handleForChannelCallback(String body, JsonNode jsonNode) {

        if (!METHOD_NAME_LIST.contains(CHANNEL_CALLBACK)) {
            return;
        }

    }

}
