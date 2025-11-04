package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.commands.UpdateUserCommand;
import com.custard.account_service.application.usecases.UpdateUserUseCase;
import com.custard.account_service.application.usecases.UseCase;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User execute(UpdateUserCommand command) {
        return userRepository.update(User.builder()
                        .id(command.getId())
                        .username(command.getUsername())
                        .email(command.getEmail())
                .build());
    }
}
