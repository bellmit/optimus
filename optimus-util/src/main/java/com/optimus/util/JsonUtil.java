package com.optimus.util;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.global.exception.OptimusException;

/**
 * JsonUtil
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * toString
     * 
     * @param object
     * @return
     */
    public static String toString(Object object) {

        if (Objects.isNull(object)) {
            return null;
        }

        String result = null;

        try {

            result = mapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "Json转换异常");
        }

        return result;

    }

    /**
     * toBean
     * 
     * @param <T>
     * @param jsonString
     * @param tClass
     * @return
     */
    public static <T> T toBean(String jsonString, Class<T> tClass) {

        if (Objects.isNull(jsonString)) {
            return null;
        }

        T result = null;

        try {

            result = mapper.readValue(jsonString, tClass);

        } catch (JsonProcessingException e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "Json转换异常");
        }

        return result;

    }

}
