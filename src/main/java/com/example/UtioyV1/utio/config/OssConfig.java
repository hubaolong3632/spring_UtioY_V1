package com.example.UtioyV1.utio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置类
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssConfig {
    /**
     * OSS endpoint（地域节点）
     * 例如：oss-cn-hangzhou.aliyuncs.com
     */
    private String endpoint;

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * Bucket名称
     */
    private String bucketName;
}


