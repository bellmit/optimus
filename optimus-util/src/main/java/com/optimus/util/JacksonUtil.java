package com.optimus.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.model.exception.OptimusException;

import lombok.extern.slf4j.Slf4j;

/**
 * JacksonUtil
 * 
 * @author sunxp
 */
@Slf4j
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
            log.error("String->JsonNode异常:", e);
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "String->JsonNode异常");
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
            log.error("Jackson->String异常:", e);
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "Jackson->String异常");
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
            log.error("Jackson->Bean异常:", e);
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "Jackson->Bean异常");
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
            log.error("String->Collection异常:", e);
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "String->Collection异常");
        }

    }

}
