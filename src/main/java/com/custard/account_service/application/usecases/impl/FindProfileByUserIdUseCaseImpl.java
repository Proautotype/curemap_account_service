package com.custard.account_service.application.usecases.impl;

import com.custard.account_service.application.usecases.FindProfileByUserIdUseCase;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.ports.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProfileByUserIdUseCaseImpl implements FindProfileByUserIdUseCase {

    private final Logger logger = LoggerFactory.getLogger(FindProfileByUserIdUseCaseImpl.class);
    private final ProfileRepository profileRepository;

    @Override
    public Profile execute(String command) {
        logger.info("Service getting profile by userID {} ", command);
        Profile profile = profileRepository.findByUserId(command);
        if (logger.isInfoEnabled()) {
            logger.info("Existing profile found with firstname -> {}",
                    profile.getFirstName().concat(" ").concat(profile.getLastName()));
        }
        return profile;
    }
}
