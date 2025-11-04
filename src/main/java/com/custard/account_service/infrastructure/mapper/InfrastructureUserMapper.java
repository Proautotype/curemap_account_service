package com.custard.account_service.infrastructure.mapper;

import com.custard.account_service.domain.models.Address;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.infrastructure.database.entities.AddressEntity;
import com.custard.account_service.infrastructure.database.entities.ProfileEntity;
import com.custard.account_service.infrastructure.database.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class InfrastructureUserMapper {
    public UserEntity toUserEntity(User user) {
        UserEntity ue = new UserEntity();
        ue.setEmail(user.getEmail());
        ue.setUsername(user.getUsername());
        return ue;
    }

    public User toUserModel(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId().toString())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .profile(userEntity.getProfile() != null ? this.toProfileModel(userEntity.getProfile()) : null)
                .build();
    }

    public ProfileEntity toProfileEntity(Profile profile) {
        ProfileEntity pe = new ProfileEntity();
        pe.setDob(profile.getDob());
        pe.setFirstName(profile.getFirstName());
        pe.setLastName(profile.getLastName());
        pe.setProfilePicture(profile.getProfilePicture());
        return pe;
    }

    public Profile toProfileModel(ProfileEntity profile) {
        return Profile.builder()
                .dob(profile.getDob())
                .userId(profile.getId())
                .profilePicture(profile.getProfilePicture())
                .lastName(profile.getLastName())
                .firstName(profile.getFirstName())
                .address(profile.getAddresses().stream().map(this::toAddressModel).toList())
                .build();
    }

    public AddressEntity toAddressEntity(Address address) {
        AddressEntity ae = new AddressEntity();
        ae.setPhone(address.getPhone());
        ae.setLocation(address.getLocation());
        return ae;
    }

    public Address toAddressModel(AddressEntity addressEntity) {
        return Address.builder()
                .location(addressEntity.getLocation())
                .phone(addressEntity.getPhone())
                .build();
    }

}
