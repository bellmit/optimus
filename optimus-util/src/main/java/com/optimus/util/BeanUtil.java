package com.optimus.util;

import java.util.List;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * BeanUtil
 * 
 * @author sunxp
 */
@Slf4j
public class BeanUtil {

    /**
     * copyProperties
     * 
     * @param <S>
     * @param <T>
     * @param sourceList
     * @param targetList
     * @param clazz
     */
    public static <S, T> void copyProperties(List<S> sourceList, List<T> targetList, Class<T> clazz) {

        sourceList.forEach(i -> {

            try {

                T t = clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(i, t);

                targetList.add(t);

            } catch (Exception e) {
                log.error("复制属性异常:", e);
                throw new OptimusException(RespCodeEnum.ERROR_CONVERT, "复制属性异常");
            }

        });

    }

}
