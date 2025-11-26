package com.example.spring_utioy_v1.utio.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文件上传
 */
@Data
@Accessors(chain = true)
public class FileModel {
    private String url; //url
    private String file_url; //文件url
    private String file_name; //文件名称
    private String size; //文件大小
    private String type; //文件类别
    private String head; //头
    private Date time; //上传时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user_id; //上传人id

}
