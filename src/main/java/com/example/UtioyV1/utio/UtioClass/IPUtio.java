package com.example.UtioyV1.utio.UtioClass;

import jakarta.servlet.http.HttpServletRequest;

public class IPUtio {
    /**
     * 获取用户真实IP地址（支持代理）
     * @param request HttpServletRequest
     * @return 用户IP地址
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isValidIP(ip)) {
            return ip;
            // X-Forwarded-For
        } else if (isValidIP(ip = request.getHeader("Proxy-Client-IP"))) {
            // Proxy-Client-IP
        } else if (isValidIP(ip = request.getHeader("WL-Proxy-Client-IP"))) {
            // WL-Proxy-Client-IP
        } else if (isValidIP(ip = request.getHeader("HTTP_CLIENT_IP"))) {
            // HTTP_CLIENT_IP
        } else if (isValidIP(ip = request.getHeader("HTTP_X_FORWARDED_FOR"))) {
            // HTTP_X_FORWARDED_FOR
        } else if (isValidIP(ip = request.getHeader("X-Real-IP"))) {
            // X-Real-IP
        } else {
            ip = request.getRemoteAddr();
        }
        // 多个代理时，第一个IP为真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 判断IP是否有效
     */
    private static boolean isValidIP(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

}
