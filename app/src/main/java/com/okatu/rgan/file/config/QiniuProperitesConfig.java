package com.okatu.rgan.file.config;

import com.okatu.rgan.file.constant.QiniuStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QiniuStorageProperties.class)
public class QiniuProperitesConfig {
}
