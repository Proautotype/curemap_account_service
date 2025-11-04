package com.custard.account_service.application.usecases;

import com.custard.account_service.application.commands.CreateProfileCommand;
import com.custard.account_service.domain.models.User;

public interface CreateProfileUseCase extends UseCase<CreateProfileCommand, User>{
}
