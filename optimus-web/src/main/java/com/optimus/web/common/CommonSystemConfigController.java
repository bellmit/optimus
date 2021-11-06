package com.optimus.web.common;

import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.web.common.req.GetCommonSystemConfigReq;
// import com.optimus.util.JsonUtil;
// import com.optimus.util.constants.RespCodeEnum;
// import com.optimus.util.global.exception.OptimusException;
import com.optimus.web.common.resp.GetCommonSystemConfigResp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * CommonSystemConfigController
 * 
 * 示例代码
 */
@RestController
@RequestMapping(value = "/optimus/common")
@Slf4j
public class CommonSystemConfigController {

    @Autowired
    private CommonSystemConfigService commonSystemConfigService;

    @GetMapping("/getCommonSystemConfigByKey")
    public GetCommonSystemConfigResp getCommonSystemConfigByKey(@RequestBody GetCommonSystemConfigReq req) {

        GetCommonSystemConfigResp resp = new GetCommonSystemConfigResp();

        String value = commonSystemConfigService.getCommonSystemConfigByKey(req.getKey());
        resp.setValue(value);

        log.info("value is {}", value);

        // 验证全局异常示例代码
        // int res = 10 / 0;
        // log.info("res is {}", res);

        // if (StringUtils.hasLength(req.getKey())) {
        // throw new OptimusException(RespCodeEnum.INVALID_PARAM);
        // }

        // jackson示例代码
        // String jsonString = JsonUtil.toString(resp);
        // log.info("bean to jsonString is {}", jsonString);
        // log.info("jsonString to bean is {}", JsonUtil.toBean(jsonString,
        // GetCommonSystemConfigResp.class));

        return resp;

    }
}
