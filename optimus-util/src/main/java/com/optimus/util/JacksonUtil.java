package com.optimus.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

/**
 * JacksonUtil
 * 
 * @author sunxp
 */
public class JacksonUtil {

    private static ObjectMapper objectMapper;

    /**
     * setObjectMapper
     * 
     * @param objectMapper
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        JacksonUtil.objectMapper = objectMapper;
    }

    /**
     * toTree
     * 
     * @param json
     * @return
     */
    public static JsonNode toTree(String json) {

        try {

            return objectMapper.readTree(json);

        } catch (Exception e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "readTree error");
        }

    }

    /**
     * toString
     * 
     * @param object
     * @return
     */
    public static String toString(Object object) {

        try {

            return objectMapper.writeValueAsString(object);

        } catch (Exception e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "object to json error");
        }

    }

    /**
     * toBean
     * 
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {

        try {

            return objectMapper.readValue(json, clazz);

        } catch (Exception e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "json to bean error");
        }

    }

    /**
     * toBean
     * 
     * @param <T>
     * @param json
     * @param typeReference
     * @return
     */
    public static <T> T toBean(String json, TypeReference<T> typeReference) {

        try {

            return objectMapper.readValue(json, typeReference);

        } catch (Exception e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "json to collection error");
        }

    }

}
