package com.custard.account_service.infrastructure.database.repositories.jpa;

import com.custard.account_service.infrastructure.database.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserEntityRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
    @Query("select u from UserEntity u where u.username like ?1")
    List<UserEntity> findByUsernameLike(String username, Pageable pageable);

    @Query("select u from UserEntity u where upper(u.username) = upper(?1)")
    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    @Query("select u from UserEntity u where upper(u.email) = upper(?1)")
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    @Query("select u from UserEntity u where u.username = ?1 and upper(u.email) = upper(?2)")
    Optional<UserEntity> findByUsernameAndEmail(String username, String email);

    boolean existsByEmail(String email);
}
