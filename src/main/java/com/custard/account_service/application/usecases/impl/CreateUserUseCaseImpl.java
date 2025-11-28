package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.application.usecases.CreateUserUseCase;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;
import com.custard.account_service.infrastructure.security.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    @Override
    public User execute(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        Profile profile = new Profile();
        profile.setFirstName(command.getFirstName());
        profile.setLastName(command.getLastName());
        User user = User.builder()
                .username(command.getUsername())
                .email(command.getEmail())
                .profile(profile)
                .build();
        keycloakUserService.createUser(command);

        return userRepository.save(user);
    }
}
