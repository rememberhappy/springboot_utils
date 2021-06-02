package com.gateway.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP地址
 *
 * @Author zhangdj
 * @Date 2021/5/31:18:52
 * @Description
 */
public class RemoteIPHost {
    /**
     * 获取IP地址
     *
     * @param request
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/5/17 17:38
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String sourceIp = null;
        String ipAddresses = request.getHeader("x-forwarded-for");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
        }
        if (!StringUtils.isEmpty(ipAddresses)) {
            sourceIp = ipAddresses.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(sourceIp) ? "127.0.0.1" : sourceIp;
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/5/31 18:58
     */
    public static String getRemoteHost(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String sourceIp = null;
        String ipAddresses = headers.getFirst("x-forwarded-for");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = headers.getFirst("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddress().getAddress().getHostAddress();
        }
        if (!StringUtils.isEmpty(ipAddresses)) {
            sourceIp = ipAddresses.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(sourceIp) ? "127.0.0.1" : sourceIp;
    }
}