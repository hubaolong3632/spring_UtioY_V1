package com.example.spring_utioy_v1.utio.controller;

import com.example.spring_utioy_v1.utio.Code.Result;
import com.example.spring_utioy_v1.utio.model.FileModel;
import com.example.spring_utioy_v1.utio.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件并自动保存图片记录
     */
    @PostMapping("/file")
    @Transactional
    public Result uploadFile(@RequestParam(value = "http", defaultValue = "false") Boolean http,
                             @RequestParam(value = "https", defaultValue = "false") Boolean https,
                             @RequestParam("file") MultipartFile file,
                             HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.failure("上传的文件为空");
        }

        FileModel fileModel = fileService.uploadFile(file, http,https, request);
        return Result.success(fileModel);
    }

    /**
     * 上传图片并压缩（有损压缩，转成JPG格式）
     * @param http 是否拼接http路径
     * @param https 是否拼接https路径
     * @param scale 缩放比例 0.0-1.0 (默认1.0不缩放, 0.5表示缩小一半)
     * @param quality 压缩质量 0.0-1.0 (默认0.8, 即80%质量)
     * @param file 图片文件
     */
    @PostMapping("/image")
    @Transactional
    public Result uploadImage(@RequestParam(value = "http", defaultValue = "false") Boolean http,
                              @RequestParam(value = "https", defaultValue = "false") Boolean https,
                              @RequestParam(value = "scale", defaultValue = "1.0") Double scale,
                              @RequestParam(value = "quality", defaultValue = "0.6") Double quality,
                              @RequestParam("file") MultipartFile file,
                              HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.failure("上传的图片为空");
        }

        FileModel fileModel = fileService.uploadImage(file, http, https, scale, quality, request);
        return Result.success(fileModel);
    }



}
