package com.example.UtioyV1.utio.LogInfo;

/**
 * 日志级别枚举
 */
public enum LogLevel {
    INFO("INFO"),
    ERROR("ERROR"),
    DEBUG("DEBUG"),
    WARN("WARN"),
    SEVERE("SEVERE");


    // 公开的属性，MyBatis 可直接访问（无需反射私有字段）
    private final String value;

    LogLevel(String value) {
        this.value = value;
    }

    // 公开的get方法（可选，也可直接访问value属性）
    public String getValue() {
        return this.value;
    }
}

