package com.example.spring_utioy_v1.utio.Code;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config")
@Data  // 添加 Lombok
public class Config {

    // static 字段（给其他地方直接用）
    public static String CLAIMS_JWS_ATTRIBUTE = "CLAIMS_JWS_ATTRIBUTE";
    public static String currentPath = System.getProperty("user.dir") + "/file";
    public static String FILE_URL = "https://smartfarmservice.00000.work";

    // 非 static 字段（可以被 JSON 序列化）
    private String currentPath1;
    private String fileUrl;

    // setter 注入后赋值给 static
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        Config.FILE_URL = fileUrl;
    }

    public void setCurrentPath(String currentPath) {
        Config.currentPath = currentPath;
    }
}