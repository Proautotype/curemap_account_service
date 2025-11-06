package com.custard.account_service.infrastructure.database.repositories;

import com.custard.account_service.application.exceptions.EntityNotFoundException;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.ProfileRepository;
import com.custard.account_service.infrastructure.database.entities.ProfileEntity;
import com.custard.account_service.infrastructure.database.entities.UserEntity;
import com.custard.account_service.infrastructure.database.repositories.jpa.JpaAddressEntityRepository;
import com.custard.account_service.infrastructure.database.repositories.jpa.JpaProfileEntityRepository;
import com.custard.account_service.infrastructure.database.repositories.jpa.JpaUserEntityRepository;
import com.custard.account_service.infrastructure.mapper.InfrastructureUserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final Logger logger = LoggerFactory.getLogger(ProfileRepositoryImpl.class.getName());
    private final JpaProfileEntityRepository profileRepository;
    private final JpaUserEntityRepository userEntityRepository;
    private final InfrastructureUserMapper infrastructureUserMapper;
    private final JpaAddressEntityRepository jpaAddressEntityRepository;

    @Override
    @Transactional
    public User save(Profile profile) {
        try {
            logger.info("Creating profile for user {} ", profile.getUserId());
            // validate
            UserEntity user = userEntityRepository
                    .findById(UUID.fromString(profile.getUserId()))
                    .orElseThrow(() -> new EntityNotFoundException("No user found"));

            Optional<ProfileEntity> profileByUserId = profileRepository.findByUser_Id(UUID.fromString(profile.getUserId()));
            // if already profile exists? update it's details else create a new profile
            ProfileEntity profileEntity = updateProfile(profile, profileByUserId.orElseGet(ProfileEntity::new));
            ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);

            User userModel = infrastructureUserMapper.toUserModel(user);
            userModel.setProfile(infrastructureUserMapper.toProfileModel(savedProfileEntity));
            return userModel;
        } catch (Exception e) {
            logger.error(e.getMessage(), e, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void updateProfileAvatar(String profileAvatarS3Key, String userId) {
        ProfileEntity profile = this.findByUserIdRaw(userId);
        profile.setProfilePicture(profileAvatarS3Key);
        profileRepository.save(profile);
    }


    private ProfileEntity updateProfile(Profile profile, ProfileEntity profileEntity) {
        if (!profile.getProfilePicture().isBlank()) {
            profileEntity.setProfilePicture(profile.getProfilePicture());
        }
        if (!profile.getFirstName().isBlank()) {
            profileEntity.setFirstName(profile.getFirstName());
        }
        if (!profile.getLastName().isBlank()) {
            profileEntity.setLastName(profile.getLastName());
        }
        if (profile.getDob() != null) {
            profileEntity.setDob(profile.getDob());
        }
        return profileEntity;
    }

    @Override
    @Transactional
    public Profile findByUserId(String userId) {
        ProfileEntity profileEntity = profileRepository
                .findByUser_Id(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        return infrastructureUserMapper.toProfileModel(profileEntity);
    }

    public ProfileEntity findByUserIdRaw(String userId) {
        return profileRepository
                .findByUser_Id(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }
}
