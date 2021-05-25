package com.bmd.oss.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.bmd.common.entity.FileBean;
import com.bmd.common.entity.responseEnum;
import com.bmd.common.result.R;
import com.bmd.oss.service.FileService;
import com.bmd.oss.utils.ossProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/oss")
public class fileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public R upload(FileBean fileBean) {
        MultipartFile file = fileBean.getFile();
        String type = fileBean.getType();
        try {
            return fileService.upload(file.getInputStream(), type, file.getOriginalFilename());
        } catch (Exception e) {
            return R.setResponseEnum(responseEnum.UPLOAD_ERROR);
        }
    }


    @GetMapping("/remove")
    public R sdfsdfsd(String fileName) {
        try {
            return fileService.remove(fileName);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }

    }


}
