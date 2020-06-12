package com.okatu.rgan.file.controller;

import com.okatu.rgan.file.constant.QiniuStorageProperties;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ObjectStorageController {

    @Autowired
    private QiniuStorageProperties qiniuStorageProperties;

    @GetMapping("/storage/token")
    public String getToken() {
        Auth auth = Auth.create(qiniuStorageProperties.getAccessKey(), qiniuStorageProperties.getSecretKey());
        int expireTime = 3600;
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        String upToken = auth.uploadToken(qiniuStorageProperties.getBucket(), null, expireTime, putPolicy);


        return upToken;
    }
}
