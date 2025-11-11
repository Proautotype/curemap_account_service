package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.commands.LoginUserCommand;
import com.custard.account_service.application.usecases.LoginUseCase;
import com.custard.account_service.infrastructure.security.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final Logger logger = LoggerFactory.getLogger(LoginUseCaseImpl.class);
    private final KeycloakUserService keycloakUserService;

    @Override
    public Map<String, Object> execute(LoginUserCommand command) {
        Map<String, Object> loginDetails = keycloakUserService.login(command.getEmail(), command.getPassword());
        logger.info("Login process completed");
        return loginDetails;
    }
}
