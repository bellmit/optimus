package com.optimus.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import com.optimus.util.constants.RespCodeEnum;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * SignUtil
 * 
 * @author sunxp
 */
public class SignUtil {

    /** 签名字段 */
    private static final String SIGN = "sign";

    /**
     * sign
     * 
     * 签名过程:
     * 
     * 对所有API参数[不含sign],依据参数名称的ASCII码表的顺序排序
     * 
     * 将已排序好的参数名和参数值拼装在一起[前后加key]
     * 
     * 将已拼装好的字符串采用UTF-8编码,使用MD5对编码后的字节流进行摘要
     * 
     * 将摘要得到的字节流使用十六进制表示
     * 
     * 
     * @param map
     * @param key
     * @return
     */
    public static String sign(Map<String, Object> map, String key) {

        AssertUtil.notEmpty(map, RespCodeEnum.ERROR_SIGN, "签名参数不能为空");
        AssertUtil.notEmpty(key, RespCodeEnum.ERROR_SIGN, "密钥不能为空");

        String[] keys = map.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        sb.append(key);

        for (String item : keys) {

            if (StringUtils.pathEquals(item, SIGN)) {
                continue;
            }

            Object value = map.get(item);

            if (StringUtils.hasLength(item)) {
                sb.append(item);
            }

            if (!Objects.isNull(value)) {
                sb.append(value);
            }

        }

        sb.append(key);

        byte[] bytes = sb.toString().toLowerCase().getBytes(StandardCharsets.UTF_8);
        return DigestUtils.md5DigestAsHex(bytes).toLowerCase();

    }

}
