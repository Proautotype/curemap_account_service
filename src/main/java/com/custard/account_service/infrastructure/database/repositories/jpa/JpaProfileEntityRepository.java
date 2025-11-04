package com.custard.account_service.infrastructure.database.repositories.jpa;

import com.custard.account_service.infrastructure.database.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaProfileEntityRepository extends JpaRepository<ProfileEntity, String>,
        JpaSpecificationExecutor<ProfileEntity> {

    @Query("select p from ProfileEntity p where p.user.id = ?1")
    Optional<ProfileEntity> findByUser_Id(UUID id);

    @Query("select p from ProfileEntity p where p.user.username = ?1")
    ProfileEntity findByUser_Username(String username);

    @Query("select p from ProfileEntity p where p.user.email = ?1")
    ProfileEntity findByUser_Email(String email);
}
