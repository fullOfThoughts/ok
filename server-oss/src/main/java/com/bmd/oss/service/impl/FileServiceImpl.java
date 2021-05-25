package com.bmd.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;

import com.bmd.common.entity.responseEnum;
import com.bmd.common.result.R;
import com.bmd.oss.service.FileService;
import com.bmd.oss.utils.ossProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public R upload(InputStream inputStream, String model, String fileName) {
        //  oss客户端
        OSS client = getClient();
        //  上传文件路径
        String key = "";
        String format = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());
        String newName = UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
        key = model + "/" + format + "/" + newName;

        client.putObject(ossProperties.BUCKET_NAME, key, inputStream);
        String filePath = "https://" + ossProperties.BUCKET_NAME + "." + ossProperties.ENDPOINT + "/" + key;
        client.shutdown();
        return R.ok().data("path", filePath);


    }

    @Override
    public R remove(String fileName) {
        //  oss客户端
        OSS client = getClient();
        System.out.println(fileName);
        //  判断文件存在
        Boolean b = client.doesObjectExist(ossProperties.BUCKET_NAME, fileName);
        if (b == false) {
            return R.setResponseEnum(responseEnum.FILE_NOT_EXITS);
        }
        client.deleteObject(ossProperties.BUCKET_NAME, fileName);
        client.shutdown();
        return R.ok();
    }

    //  获取客户端
    private OSS getClient() {
        OSS client = new OSSClientBuilder().build(ossProperties.ENDPOINT, ossProperties.KEY_ID, ossProperties.KEY_SECRET);
        //  判断bucket是否存在
        if (!client.doesBucketExist(ossProperties.BUCKET_NAME)) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(ossProperties.BUCKET_NAME);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            client.createBucket(createBucketRequest);
        }
        return client;
    }


}
