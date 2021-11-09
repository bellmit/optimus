package com.optimus.web.common;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.service.common.CommonSystemConfigService;
import com.optimus.web.common.req.GetCommonSystemConfigReq;
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
     * http://127.0.0.1:8080/optimus/common/getCommonSystemConfig
     * 
     * {"key": "ORDER_RELEASE_TIME", "date": "2021-11-23 08:00:00", "sign":
     * "7f4a0c5740838447b71e76c6fb6e2d13", "memberId": "100001"}
     * 
     * @param req
     * @return
     */
    @GetMapping("/getCommonSystemConfig")
    public GetCommonSystemConfigResp getCommonSystemConfig(@RequestBody GetCommonSystemConfigReq req) {

        GetCommonSystemConfigResp resp = new GetCommonSystemConfigResp();

        CommonSystemConfigValidate.validateGetCommonSystemConfig(req);

        String value = commonSystemConfigService.getCommonSystemConfigByKey(req.getKey());
        resp.setValue(value);

        return resp;

    }

    /**
     * 示例代码
     */
    protected void demo() {

        GetCommonSystemConfigResp resp = new GetCommonSystemConfigResp();
        resp.setValue("10");

        // 全局异常示例代码
        int res = 10 / 0;
        log.info("res is {}", res);

        // jackson示例代码
        String jsonString = JsonUtil.toString(resp);
        log.info("bean to jsonString is {}", jsonString);
        log.info("jsonString to bean is {}", JsonUtil.toBean(jsonString, GetCommonSystemConfigResp.class));

        List<GetCommonSystemConfigResp> respList = new ArrayList<>();
        respList.add(new GetCommonSystemConfigResp("1"));
        respList.add(new GetCommonSystemConfigResp("2"));

        jsonString = JsonUtil.toString(respList);
        log.info("bean to jsonString is {}", jsonString);

        respList = JsonUtil.toBean(jsonString, new TypeReference<List<GetCommonSystemConfigResp>>() {
        });
        log.info("jsonString to collect is {}", respList);

    }
}
