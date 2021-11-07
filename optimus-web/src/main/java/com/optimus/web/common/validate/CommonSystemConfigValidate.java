package com.optimus.web.common.validate;

import java.util.Objects;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.global.exception.OptimusException;
import com.optimus.web.common.req.GetCommonSystemConfigReq;

import org.springframework.util.StringUtils;

/**
 * CommonSystemConfigValidate
 */
public class CommonSystemConfigValidate {

    /**
     * validateGetCommonSystemConfig
     * 
     * @param req
     */
    public static void validateGetCommonSystemConfig(GetCommonSystemConfigReq req) {

        if (Objects.isNull(req)) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "入参对象不能为空");
        }

        if (!StringUtils.hasLength(req.getKey())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "key不能为空");
        }

    }
}
