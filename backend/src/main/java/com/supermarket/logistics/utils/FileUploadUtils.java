package com.supermarket.logistics.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传工具类
 * 
 * 作用：处理文件上传，支持上传到阿里云OSS对象存储
 * 
 * 主要功能：
 * 1. 生成唯一文件名（UUID + 日期 + 原文件名）
 * 2. 上传文件到阿里云OSS
 * 3. 返回文件访问URL
 */
@Component
public class FileUploadUtils {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Value("${aliyun.oss.enabled:true}")
    private boolean ossEnabled;

    /**
     * 上传文件到阿里云OSS
     * 
     * @param file 上传的文件对象
     * @param module 模块名称，用于区分不同业务模块的文件（如goods、user等）
     * @return 文件访问URL，上传失败返回null
     */
    public String uploadFile(MultipartFile file, String module) {
        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }
        
        // 生成UUID和日期字符串，确保文件名唯一
        String uuid = UUID.randomUUID().toString();
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        
        // 重新组装文件名：UUID-日期-原文件名.扩展名
        int dotIndex = originalFilename.lastIndexOf(".");
        String fileName;
        if (dotIndex > 0) {
            String nameWithoutExt = originalFilename.substring(0, dotIndex);
            String extension = originalFilename.substring(dotIndex);
            fileName = uuid + "-" + dateStr + "-" + nameWithoutExt + extension;
        } else {
            fileName = uuid + "-" + dateStr + "-" + originalFilename;
        }
        
        // 拼接OSS存储路径：模块名/文件名
        String objectName = module + "/" + fileName;
        
        try {
            // 判断是否启用OSS上传
            if (ossEnabled) {
                return aliOssUtil.uploadFile(objectName, file.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
