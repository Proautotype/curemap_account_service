package com.custard.account_service.domain.ports;

import com.custard.account_service.domain.models.User;

public interface UserRepository {
    User save(User user);
    User update(User user);
    User findById(String id);
    User findByEmail(String email);
    void deleteById(String id);
    boolean existsByEmail(String email);
}