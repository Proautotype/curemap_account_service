package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.usecases.RefreshTokenUseCase;
import com.custard.account_service.infrastructure.config.security.KeycloakProperties;
import com.custard.account_service.infrastructure.security.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {
    private final Logger logger = LoggerFactory.getLogger(RefreshTokenUseCaseImpl.class);
    private final KeycloakProperties keycloakProperties;
    private final KeycloakUserService keycloakUserService;

    @Override
    public Map<String, Object> execute(String refreshToken) {
        return keycloakUserService.refresh(refreshToken);
    }
}
