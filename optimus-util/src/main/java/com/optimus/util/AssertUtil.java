package com.optimus.util;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    /**
     * empty
     *
     * @param object
     * @param respCodeEnum
     * @param memo
     */
    public static void empty(Object object, RespCodeEnum respCodeEnum, String memo) {

        if (ObjectUtils.isEmpty(object)) {
            return;
        }

        throw new OptimusException(respCodeEnum, memo);

    }

    /**
     * notEquals
     * 
     * @param arg0
     * @param arg1
     * @param respCodeEnum
     * @param memo
     */
    public static void notEquals(String arg0, String arg1, RespCodeEnum respCodeEnum, String memo) {

        if (!StringUtils.hasLength(arg0) || !StringUtils.hasLength(arg1)) {
            throw new OptimusException(respCodeEnum, memo);
        }

        if (StringUtils.pathEquals(arg0, arg1)) {
            return;
        }

        throw new OptimusException(respCodeEnum, memo);

    }

    /**
     * equals
     * 
     * @param arg0
     * @param arg1
     * @param respCodeEnum
     * @param memo
     */
    public static void equals(String arg0, String arg1, RespCodeEnum respCodeEnum, String memo) {

        if (!StringUtils.hasLength(arg0) || !StringUtils.hasLength(arg1)) {
            throw new OptimusException(respCodeEnum, memo);
        }

        if (!StringUtils.pathEquals(arg0, arg1)) {
            return;
        }

        throw new OptimusException(respCodeEnum, memo);

    }

}
