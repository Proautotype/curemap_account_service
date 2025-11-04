package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.application.usecases.CreateUserUseCase;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User execute(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = User.builder()
                .username(command.getUsername())
                .email(command.getEmail())
                .build();

        return userRepository.save(user);
    }
}
