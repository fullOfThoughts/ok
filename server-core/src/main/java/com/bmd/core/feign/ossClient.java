package com.bmd.core.feign;

import com.bmd.common.entity.FileBean;
import com.bmd.common.result.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "server-oss")
public interface ossClient {
    //  保存
    @PostMapping( value = "/oss/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     R upload(FileBean fileBean);

    //  删除
    @GetMapping("/oss/remove")
    public R remove(@RequestParam("fileName") String fileName);
}
