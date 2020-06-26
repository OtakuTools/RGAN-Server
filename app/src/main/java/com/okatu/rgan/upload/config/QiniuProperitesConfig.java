package com.okatu.rgan.upload.config;

import com.okatu.rgan.upload.constant.QiniuStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QiniuStorageProperties.class)
public class QiniuProperitesConfig {
}
