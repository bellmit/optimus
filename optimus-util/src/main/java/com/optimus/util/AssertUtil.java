package com.optimus.util;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.util.ObjectUtils;

/**
 * AssertUtil
 * 
 * @author sunxp
 */
public class AssertUtil {

    /**
     * notEmpty
     * 
     * @param object
     * @param respCodeEnum
     * @param memo
     */
    public static void notEmpty(Object object, RespCodeEnum respCodeEnum, String memo) {

        if (!ObjectUtils.isEmpty(object)) {
            return;
        }

        throw new OptimusException(respCodeEnum, memo);

    }

}
