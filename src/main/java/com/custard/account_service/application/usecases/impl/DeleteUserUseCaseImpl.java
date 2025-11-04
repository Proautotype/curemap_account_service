package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.commands.DeleteUserCommand;
import com.custard.account_service.application.usecases.DeleteUserUseCase;
import com.custard.account_service.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Boolean execute(DeleteUserCommand command) {
         try{
             userRepository.deleteById(command.getId());
             return true;
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }
}
