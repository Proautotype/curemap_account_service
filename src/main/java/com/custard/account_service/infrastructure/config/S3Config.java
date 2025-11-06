package com.custard.account_service.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
@ToString
public class S3Config {
    private String accessKeyId;
    private String secretAccessKey;
    private String region;
    private String bucketName;
}
