package com.example.UtioyV1.utio.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义请求包装器 - 可以添加自定义请求参数
 */
public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> customParams = new HashMap<>();

    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
        // 复制原有参数
        this.customParams.putAll(request.getParameterMap());
    }

    /**
     * 添加自定义参数
     */
    public void addParameter(String name, String value) {
        customParams.put(name, new String[]{value});
    }

    @Override
    public String getParameter(String name) {
        String[] values = customParams.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return customParams;
    }

    @Override
    public String[] getParameterValues(String name) {
        return customParams.get(name);
    }
}