package com.custard.account_service.application.usecases;

import com.custard.account_service.application.commands.UpdateUserCommand;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;

import java.util.NoSuchElementException;

public interface UpdateUserUseCase extends UseCase<UpdateUserCommand, User> {

}
