package com.optimus.runner.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.service.member.MemberService;
import com.optimus.service.member.dto.MemberInfoDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.JsonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.MemberEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.req.Req;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
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
    public static final List<String> METHOD_NAME_LIST = Arrays.asList("channelCallback");

    /** 特例方法-渠道回调平台 */
    public static final String CHANNEL_CALLBACK = "channelCallback";

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

        byte[] bytes = new byte[arg0.getBody().available()];
        arg0.getBody().read(bytes);
        String body = new String(bytes);

        log.info("{}.{} is {}", arg1.getMethod().getDeclaringClass().getSimpleName(), arg1.getMethod().getName(), body);

        JsonNode jsonNode = JsonUtil.toTree(body);

        handleForBase(body, jsonNode);
        handleForChannelCallback(body, jsonNode);

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
     * 基础处理
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
        if (!StringUtils.hasLength(sign)) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "签名串不能为空");
        }
        if (!StringUtils.hasLength(method)) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "方法名不能为空");
        }
        if (timestamp == null) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳不能为空");
        }
        if (currentDate.compareTo(timestamp) < 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳大于当前时间");
        }
        if (DateUtil.offsetForMinute(currentDate, -1).compareTo(timestamp) > 0) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "时间戳已过期");
        }
        if (!StringUtils.hasLength(memberId)) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "会员编号不能为空");
        }

        List<MemberInfoDTO> memberInfoList = memberService.getMemberInfoList(new MemberInfoDTO(memberId));
        if (CollectionUtils.isEmpty(memberInfoList)) {
            throw new OptimusException(RespCodeEnum.MEMBER_NO);
        }
        if (memberInfoList.size() != 1) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR);
        }

        MemberInfoDTO memberInfoDTO = memberInfoList.get(0);
        if (!MemberEnum.MEMBER_STATUS_Y.getCode().equals(memberInfoDTO.getMemberStatus())) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员无效");
        }

        Map<String, String> map = JsonUtil.toBean(body, new TypeReference<Map<String, String>>() {
        });

        String basisSign = SignUtil.sign(map, memberInfoDTO.getMemberKey());
        if (!sign.equals(basisSign)) {
            throw new OptimusException(RespCodeEnum.ERROR_SIGN);
        }

    }

    /**
     * 网关渠道回调平台处理
     * 
     * @param jsonNode
     */
    private void handleForChannelCallback(String body, JsonNode jsonNode) {

        if (!METHOD_NAME_LIST.contains(CHANNEL_CALLBACK)) {
            return;
        }

    }

}
