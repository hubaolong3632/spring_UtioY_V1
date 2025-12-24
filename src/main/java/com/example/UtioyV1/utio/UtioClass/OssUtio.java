package com.example.UtioyV1.utio.UtioClass;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.UtioyV1.utio.config.OssConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 阿里云OSS工具类
 */
@Component
public class OssUtio {

    @Resource
    private OssConfig ossConfig;

    /**
     * 创建OSS客户端
     * @return OSS客户端实例
     */
    private OSS createOssClient() {
        return new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );
    }

    /**
     * 上传文件到OSS
     * @param inputStream 文件输入流
     * @param objectName OSS中的对象名称（文件路径，例如：file/2024/1/15/test.jpg）
     * @return 文件的访问URL
     */
    public String uploadFile(InputStream inputStream, String objectName) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient();
            
            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    objectName,
                    inputStream
            );
            
            // 上传文件
            ossClient.putObject(putObjectRequest);
            
            // 构建文件访问URL
            // 格式：https://bucket-name.endpoint/object-name
            String url = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + objectName;
            
            return url;
        } catch (Exception e) {
            throw new RuntimeException("OSS上传失败: " + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除OSS中的文件
     * @param objectName OSS中的对象名称
     */
    public void deleteFile(String objectName) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient();
            ossClient.deleteObject(ossConfig.getBucketName(), objectName);
        } catch (Exception e) {
            throw new RuntimeException("OSS删除失败: " + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 判断文件是否存在
     * @param objectName OSS中的对象名称
     * @return true-存在，false-不存在
     */
    public boolean fileExists(String objectName) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient();
            return ossClient.doesObjectExist(ossConfig.getBucketName(), objectName);
        } catch (Exception e) {
            throw new RuntimeException("OSS检查文件失败: " + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}

