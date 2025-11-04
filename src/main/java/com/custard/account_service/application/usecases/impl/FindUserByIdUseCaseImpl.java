package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.usecases.FindUserByIdUseCase;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByIdUseCaseImpl implements FindUserByIdUseCase {
    private final UserRepository userRepository;
    @Override
    public User execute(String command) {
        return userRepository.findById(command);
    }
}
