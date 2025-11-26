package com.example.spring_utioy_v1.utio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器路径配置
 */
@Component
@ConfigurationProperties(prefix = "interceptor")
public class InterceptorConfig {

    /**
     * 拦截的路径
     */
    private List<String> includePaths = new ArrayList<>();

    /**
     * 排除的路径
     */
    private List<String> excludePaths = new ArrayList<>();

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }

    public List<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }
}

