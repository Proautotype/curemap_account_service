package com.custard.account_service.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "files")
@Getter
@Setter
public class FileUploadConfig {
    private List<String> acceptedImages;
    private List<String> acceptedAudios;
    private FileLimits fileLimits;

    @Getter
    @Setter
    public static class FileLimits {
        private int maxImageSizeMB;
        private int maxAudioSizeMB;
    }
}