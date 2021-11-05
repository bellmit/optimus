package com.optimus.web.common;

import com.optimus.service.common.CommonSystemConfigService;
// import com.optimus.util.constants.RespCodeEnum;
// import com.optimus.util.global.exception.OptimusException;
import com.optimus.web.common.resp.GetCommonSystemConfigResp;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public GetCommonSystemConfigResp getCommonSystemConfigByKey(@RequestParam("key") String key) {

        log.info("start getCommonSystemConfigByKey, key is {}", key);

        GetCommonSystemConfigResp resp = new GetCommonSystemConfigResp();

        // 验证全局异常示例代码
        // int res = 10 / 0;
        // log.info("res is {}", res);

        // if (StringUtils.hasLength(key)) {
        // throw new OptimusException(RespCodeEnum.INVALID_PARAM);
        // }

        String value = commonSystemConfigService.getCommonSystemConfigByKey(key);
        resp.setValue(value);

        return resp;

    }
}
