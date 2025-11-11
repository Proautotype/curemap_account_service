package com.custard.account_service.infrastructure.clients;

import com.custard.account_service.infrastructure.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Cli3nt {
    private final S3Config config;
}
