package com.supermarket.logistics.controller;

import com.supermarket.logistics.common.Result;
import com.supermarket.logistics.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {
    
    @Autowired
    private FileUploadUtils fileUploadUtils;
    
    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String imagePath = fileUploadUtils.uploadFile(file, "avatar");
        if (imagePath != null) {
            return Result.success("上传成功", imagePath);
        }
        return Result.error("上传失败");
    }
    
    /**
     * 上传商品图片
     */
    @PostMapping("/goods")
    public Result<String> uploadGoodsImage(@RequestParam("file") MultipartFile file) {
        String imagePath = fileUploadUtils.uploadFile(file, "goods");
        if (imagePath != null) {
            return Result.success("上传成功", imagePath);
        }
        return Result.error("上传失败");
    }
    
    /**
     * 通用文件上传
     */
    @PostMapping("/file")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file,
                                      @RequestParam(defaultValue = "common") String module) {
        String filePath = fileUploadUtils.uploadFile(file, module);
        if (filePath != null) {
            return Result.success("上传成功", filePath);
        }
        return Result.error("上传失败");
    }
}
