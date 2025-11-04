package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.commands.CreateProfileCommand;
import com.custard.account_service.application.mapper.Application_UserMapper;
import com.custard.account_service.application.usecases.CreateProfileUseCase;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProfileUseCaseImpl implements CreateProfileUseCase {

    private final ProfileRepository profileRepository;
    private final Application_UserMapper applicationUserMapper;

    @Override
    public User execute(CreateProfileCommand command) {
        Profile profile = applicationUserMapper.toProfile(command);
        profileRepository.save(profile);
        return null;
    }
}
