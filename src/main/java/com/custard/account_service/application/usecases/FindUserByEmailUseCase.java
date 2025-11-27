package com.custard.account_service.application.usecases;

import com.custard.account_service.domain.models.User;
import reactor.core.publisher.Mono;

public interface FindUserByEmailUseCase extends UseCase<String, Mono<User>> {
}
