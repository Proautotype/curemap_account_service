package com.custard.account_service.domain.ports;

import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;

public interface ProfileRepository {
    User save(Profile profile);
    void updateProfileAvatar(String keyName, String userId);
    Profile findByUserId(String userId);
}