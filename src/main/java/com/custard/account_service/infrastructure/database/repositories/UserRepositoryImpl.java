package com.custard.account_service.infrastructure.database.repositories;

import com.custard.account_service.application.exceptions.EntityNotFoundException;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.domain.ports.UserRepository;
import com.custard.account_service.infrastructure.database.entities.ProfileEntity;
import com.custard.account_service.infrastructure.database.entities.UserEntity;
import com.custard.account_service.infrastructure.database.repositories.jpa.JpaProfileEntityRepository;
import com.custard.account_service.infrastructure.database.repositories.jpa.JpaUserEntityRepository;
import com.custard.account_service.infrastructure.mapper.InfrastructureUserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class.getName());

    private final InfrastructureUserMapper userMapper;

    private final JpaUserEntityRepository jpaUserEntityRepository;
    private final JpaProfileEntityRepository jpaProfileEntityRepository;

    @Override
    @Transactional
    public User save(User user) {
        logger.info("create a new user");
        try {
            // create user entity
            UserEntity userEntity = userMapper.toUserEntity(user);
            UserEntity ue = jpaUserEntityRepository.save(userEntity);


            // create default profile for user
            ProfileEntity pe = new ProfileEntity();
            pe.setUser(ue);
            pe.setFirstName(user.getUsername());
            jpaProfileEntityRepository.save(pe);

            return findById(ue.getId().toString());
        } catch (RuntimeException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

    @Override
    public User update(User user) {
        try {
            UserEntity userEntity = jpaUserEntityRepository.findById(UUID.fromString(user.getId())).orElseThrow();
            if (user.getEmail() != null) {
                userEntity.setEmail(user.getEmail());
            }
            if (user.getUsername() != null) {
                userEntity.setEmail(user.getUsername());
            }
            return userMapper.toUserModel(userEntity);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(String id) {
        logger.info("find a new user by ID");
        UserEntity user = jpaUserEntityRepository
                .findById(UUID.fromString(id))
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        return userMapper.toUserModel(user);
    }

    @Override
    public User findByEmail(String email) {
        logger.info("fine a new user by email");
        UserEntity user = jpaUserEntityRepository.findByEmailIgnoreCase(email).orElseThrow();
        return userMapper.toUserModel(user);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        try {
            logger.info("delete a new user by id");
            UserEntity user = jpaUserEntityRepository.findById(UUID.fromString(id)).orElseThrow();
            jpaUserEntityRepository.delete(user);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserEntityRepository.existsByEmail(email);
    }
}
