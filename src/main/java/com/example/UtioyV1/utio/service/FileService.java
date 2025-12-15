package com.example.UtioyV1.utio.service;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.model.FileModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class FileService {

    /**
     * 上传文件
     * @param file 上传的文件
     * @param http 是否拼接http路径
     * @param request 请求对象
     * @return FileModel
     */
    public FileModel uploadFile(MultipartFile file, Boolean http, Boolean https, HttpServletRequest request) {
        // 1. 处理文件名（添加时间戳防止重复）
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new ReturnException("文件名格式不正确");
        }

        // 文件访问头拼接地址
        String url = Config.FILE_URL;

        long currentTimeMillis = System.currentTimeMillis();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = "F" + currentTimeMillis+ fileExtension;

        // 2. 配置文件存储路径（按年/月/日划分）
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        String file_day = "/file/" + year + "/" + month + "/" + day + "/";

        String basePath = Config.currentPath + file_day;

        File storageDir = new File(basePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        try {
            // 3. 保存文件到服务器
            Path targetPath = Paths.get(basePath + fileName);
            Files.copy(file.getInputStream(), targetPath);
        } catch (Exception e) {
            throw new ReturnException("文件保存失败: " + e.getMessage());
        }

        // 4. 构建可访问的URL
        if (!http) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            if (https) {
                url=url.replace("http","https");
            }
        }

        String contextPath = request.getContextPath();
        String path = contextPath + file_day;

        // 获取文件大小和类型
        long fileSize = file.getSize();
        String contentType = file.getContentType();

        FileModel fileModel = new FileModel();
        fileModel
                .setFile_name(fileName)      //文件名称
                .setFile_url(path+fileName)       //文件url
                .setUrl(url+path+fileName)     //文件路径

                .setSize(formatFileSize(fileSize))  // 格式化后的大小
                .setType(contentType)        // 文件类型
                .setTime(new Date())        // 文件类型
                .setHead(url)        // 头
        ;

        return fileModel;
    }

    /**
     * 格式化文件大小
     * @param size 字节数
     * @return 格式化后的大小字符串（如 1.5MB, 500KB）
     */
    public String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2fKB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2fGB", size / (1024.0 * 1024 * 1024));
        }
    }

    // 支持的图片格式
    private static final List<String> IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"
    );

    /**
     * 上传图片并压缩（转成JPG格式进行有损压缩）
     * @param file 上传的图片文件
     * @param http 是否拼接http路径
     * @param https 是否拼接https路径
     * @param scale 缩放比例 0.0-1.0 (例如 0.5 表示缩小一半，1.0 表示原尺寸)
     * @param quality 压缩质量 0.0-1.0 (例如 0.8 表示80%质量)
     * @param request 请求对象
     * @return FileModel
     */
    public FileModel uploadImage(MultipartFile file, Boolean http, Boolean https, Double scale, Double quality, HttpServletRequest request) {
        // 1. 校验是否为图片
        String contentType = file.getContentType();
        if (contentType == null || !IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new ReturnException("只支持上传图片格式: jpg, png, gif, bmp, webp");
        }

        // 2. 处理文件名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new ReturnException("文件名格式不正确");
        }

        String url = Config.FILE_URL;
        long currentTimeMillis = System.currentTimeMillis();

        // 3. 配置文件存储路径（按年/月/日划分）
        LocalDate today = LocalDate.now();
        String file_day = "/file/" + today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth() + "/";
        String basePath = Config.currentPath + file_day;

        File storageDir = new File(basePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        try {
            // 4. 读取图片
            BufferedImage inputImage = ImageIO.read(file.getInputStream());
            if (inputImage == null) {
                throw new ReturnException("无法读取图片文件");
            }

            // 默认缩放比例 1.0（不缩放）
            if (scale == null || scale <= 0 || scale > 1) {
                scale = 1.0;
            }

            // 默认压缩质量 0.8
            if (quality == null || quality <= 0 || quality > 1) {
                quality = 0.8;
            }

            // 缩放图片
            BufferedImage scaledImage = compressImage(inputImage, scale.floatValue());

            // 转换为RGB模式（去除Alpha通道，PNG转JPG必须）
            BufferedImage rgbImage = new BufferedImage(
                    scaledImage.getWidth(),
                    scaledImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            rgbImage.createGraphics().drawImage(scaledImage, 0, 0, Color.WHITE, null);

            // 统一输出为 JPG 格式
            String fileName = "IMG" + currentTimeMillis + ".jpg";
            File targetFile = new File(basePath + fileName);

            // 使用 JPG 质量压缩
            compressToJpg(rgbImage, targetFile, quality.floatValue());

            // 5. 构建可访问的URL
            if (!http) {
                url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                if (https) {
                    url = url.replace("http", "https");
                }
            }

            String contextPath = request.getContextPath();
            String path = contextPath + file_day;

            // 获取压缩后的文件大小
            long fileSize = targetFile.length();

            FileModel fileModel = new FileModel();
            fileModel
                    .setFile_name(fileName)
                    .setFile_url(path + fileName)
                    .setUrl(url + path + fileName)
                    .setSize(formatFileSize(fileSize))
                    .setType("image/jpeg")
                    .setTime(new Date())
                    .setHead(url)        // 头
            ;

            return fileModel;

        } catch (Exception e) {
            throw new ReturnException("图片压缩保存失败: " + e.getMessage());
        }
    }

    /**
     * 压缩并保存为JPG格式
     * @param image 图片
     * @param outputFile 输出文件
     * @param quality 压缩质量 0.0-1.0
     */
    private void compressToJpg(BufferedImage image, File outputFile, float quality) throws Exception {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new ReturnException("不支持JPG格式");
        }

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        // 设置压缩模式和质量
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    /**
     * 压缩图片（按比例缩放）
     * @param inputImage 原始图片
     * @param scale 缩放比例
     * @return 压缩后的图片
     */
    private BufferedImage compressImage(BufferedImage inputImage, float scale) {
        int outputWidth = (int) (inputImage.getWidth() * scale);
        int outputHeight = (int) (inputImage.getHeight() * scale);
        BufferedImage outputImage = new BufferedImage(outputWidth, outputHeight, inputImage.getType());
        outputImage.getGraphics().drawImage(
                inputImage.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH), 0, 0, null);
        return outputImage;
    }

    /**
     * 根据图片类型返回格式名称
     * @param imageType BufferedImage 的类型
     * @return 格式名称
     */
    private String getImageType(int imageType) {
        switch (imageType) {
            case 6:
                return "png";
            case 13:
                return "gif";
            default:
                return "jpg";
        }
    }


    /**
     * 无损压缩图片（PNG格式，最大压缩级别）
     * @param image 图片
     * @param outputFile 输出文件
     */
    private void compressLossless(BufferedImage image, File outputFile) throws Exception {
        // 获取 PNG 写入器
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
        if (!writers.hasNext()) {
            throw new ReturnException("不支持PNG格式");
        }

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        // PNG 支持压缩模式设置
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.0f); // 0.0 = 最大压缩，1.0 = 无压缩
        }

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

}
