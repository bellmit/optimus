package com.optimus.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * SignUtil
 * 
 * @author sunxp
 */
public class SignUtil {

    public static final String SIGN = "sign";

    /**
     * sign
     * 
     * 签名过程:
     * 
     * 对所有API请求参数[不含sign],依据参数名称的ASCII码表的顺序排序
     * 
     * 将已排序好的参数名和参数值拼装在一起[前后加key]
     * 
     * 将已拼装好的字符串采用UTF-8编码，使用MD5对编码后的字节流进行摘要
     * 
     * 将摘要得到的字节流使用十六进制表示
     * 
     * 
     * @param map
     * @param key
     * @return
     */
    public static String sign(Map<String, String> map, String key) {

        if (CollectionUtils.isEmpty(map)) {
            throw new OptimusException(RespCodeEnum.ERROR_SIGN, "签名参数为空");
        }

        if (!StringUtils.hasLength(key)) {
            throw new OptimusException(RespCodeEnum.ERROR_SIGN, "密钥为空");
        }

        String[] keys = map.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        sb.append(key);

        for (String item : keys) {

            if (SIGN.equals(item)) {
                continue;
            }

            String value = map.get(item);

            if (StringUtils.hasLength(item)) {
                sb.append(item);
            }

            if (StringUtils.hasLength(value)) {
                sb.append(value);
            }

        }

        sb.append(key);

        byte[] bytes = sb.toString().toLowerCase().getBytes(StandardCharsets.UTF_8);
        return DigestUtils.md5DigestAsHex(bytes).toLowerCase();

    }

}
