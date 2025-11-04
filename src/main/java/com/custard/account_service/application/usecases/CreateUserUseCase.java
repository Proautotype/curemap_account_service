package com.custard.account_service.application.usecases;

import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.domain.models.User;

public interface CreateUserUseCase extends UseCase<CreateUserCommand, User> {

}
