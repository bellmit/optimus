package com.optimus.util;

import javax.servlet.http.HttpServletRequest;

import com.optimus.util.constants.BaseEnum;

import org.springframework.util.StringUtils;

/**
 * HostUtil
 * 
 * @author sunxp
 */
public class HostUtil {

    /**
     * getRemoteIp
     * 
     * @param req
     * @return
     */
    public static String getRemoteIp(HttpServletRequest req) {

        // Squid
        String ip = req.getHeader("X-Forwarded-For");

        // Apache
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }

        // Weblogic
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }

        // Nginx
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("X-Real-IP");
        }

        // 其他
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }

        // 匹配ip
        if (StringUtils.hasLength(ip)) {
            String[] ips = ip.split(",");

            for (String item : ips) {
                if (BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(item)) {
                    continue;
                }

                ip = item;
                break;
            }
        }

        // 兜底
        if (!StringUtils.hasLength(ip) || BaseEnum.UNKNOWN.getCode().equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }

        return ip;

    }

}
