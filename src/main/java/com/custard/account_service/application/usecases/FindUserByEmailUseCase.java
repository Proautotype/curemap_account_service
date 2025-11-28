package com.custard.account_service.application.usecases;

import com.custard.account_service.domain.models.User;

public interface FindUserByEmailUseCase extends UseCase<String, User> {
}
