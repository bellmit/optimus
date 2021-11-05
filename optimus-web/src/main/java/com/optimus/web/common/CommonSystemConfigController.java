package com.optimus.web.common;

import com.optimus.service.common.CommonSystemConfigService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String getCommonSystemConfigByKey(@RequestParam("key") String key) {

        log.info("start getCommonSystemConfigByKey, key is {}", key);

        String value = null;

        try {

            value = commonSystemConfigService.getCommonSystemConfigByKey(key);

        } catch (Exception e) {
            log.error("getCommonSystemConfigByKey is error", e);
        }

        return value;

    }
}
