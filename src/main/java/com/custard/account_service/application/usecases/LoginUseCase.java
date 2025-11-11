package com.custard.account_service.application.usecases;

import com.custard.account_service.application.commands.LoginUserCommand;

import java.util.Map;

public interface LoginUseCase extends UseCase<LoginUserCommand, Map<String, Object>> {
}
