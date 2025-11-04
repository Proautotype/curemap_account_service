package com.custard.account_service.domain.ports;

import com.custard.account_service.domain.models.Address;
import java.util.Optional;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(String id);
    Optional<Address> findByUserId(String userId);
    void deleteById(String id);
}