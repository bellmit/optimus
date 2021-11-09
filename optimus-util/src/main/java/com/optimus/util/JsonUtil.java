package com.optimus.util;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

/**
 * JsonUtil
 * 
 * @author sunxp
 */
public class JsonUtil {

    private static ObjectMapper objectMapper;

    /**
     * setObjectMapper
     * 
     * @param objectMapper
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
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

        } catch (JsonProcessingException e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "readTree转换异常");
        }

    }

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

        try {

            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "writeValueAsString转换异常");
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

        if (Objects.isNull(json)) {
            return null;
        }

        try {

            return objectMapper.readValue(json, clazz);

        } catch (JsonProcessingException e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "readValue转换异常");
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

        if (Objects.isNull(json) || Objects.isNull(typeReference)) {
            return null;
        }

        if (typeReference.getType().equals(String.class)) {
            return null;
        }

        try {

            return objectMapper.readValue(json, typeReference);

        } catch (IOException e) {
            throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "readValue转换异常");
        }

    }

}
