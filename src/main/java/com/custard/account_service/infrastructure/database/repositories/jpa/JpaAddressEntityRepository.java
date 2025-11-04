package com.custard.account_service.infrastructure.database.repositories.jpa;

import com.custard.account_service.infrastructure.database.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAddressEntityRepository extends JpaRepository<AddressEntity, String>,
        JpaSpecificationExecutor<AddressEntity> {
}
