package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.usecases.FindUserByEmailUseCase;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindUserByEmailUseCaseImpl implements FindUserByEmailUseCase {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(FindUserByEmailUseCaseImpl.class);

    @Override
    public Mono<User> execute(String command) {
        logger.info("finding user by email {} ", command);
        return Mono
                .fromCallable(() -> userRepository.findByEmail(command))
                .doOnSuccess((user -> {
                    logger.info("finding user by email ");
                }));
    }
}
