package com.optimus.web.common;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.web.common.req.GetCommonSystemConfigReq;
import com.optimus.util.DateUtil;
import com.optimus.util.JsonUtil;
import com.optimus.web.common.resp.GetCommonSystemConfigResp;
import com.optimus.web.common.validate.CommonSystemConfigValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * CommonSystemConfigController
 * 
 * 示例代码
 * 
 * @author sunxp
 */
@RestController
@RequestMapping(value = "/optimus/common")
@Slf4j
public class CommonSystemConfigController {

    @Autowired
    private CommonSystemConfigService commonSystemConfigService;

    /**
     * getCommonSystemConfig
     * 
     * @param req
     * @return
     */
    @GetMapping("/getCommonSystemConfig")
    public GetCommonSystemConfigResp getCommonSystemConfig(@RequestBody GetCommonSystemConfigReq req) {

        GetCommonSystemConfigResp resp = new GetCommonSystemConfigResp();

        CommonSystemConfigValidate.validateGetCommonSystemConfig(req);

        String value = commonSystemConfigService.getCommonSystemConfigByBaseKey(req.getBaseKey());
        resp.setValue(value);
        resp.setDate(DateUtil.currentDate());

        return resp;

    }

    protected void demo(GetCommonSystemConfigResp resp) {

        // 验证全局异常示例代码
        int res = 10 / 0;
        log.info("res is {}", res);

        // jackson示例代码
        String jsonString = JsonUtil.toString(resp);
        log.info("bean to jsonString is {}", jsonString);
        log.info("jsonString to bean is {}", JsonUtil.toBean(jsonString, GetCommonSystemConfigResp.class));

        List<GetCommonSystemConfigResp> respList = new ArrayList<>();
        respList.add(new GetCommonSystemConfigResp("1", null));
        respList.add(new GetCommonSystemConfigResp("2", null));

        jsonString = JsonUtil.toString(respList);
        log.info("bean to jsonString is {}", jsonString);

        respList = JsonUtil.toBean(jsonString, new TypeReference<List<GetCommonSystemConfigResp>>() {
        });
        log.info("jsonString to collect is {}", respList);

    }

}
