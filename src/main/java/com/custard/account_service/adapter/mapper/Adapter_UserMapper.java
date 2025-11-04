package com.custard.account_service.adapter.mapper;

import com.custard.account_service.adapter.dto.AddressDto;
import com.custard.account_service.adapter.dto.ProfileDto;
import com.custard.account_service.adapter.dto.UserDto;
import com.custard.account_service.domain.models.Address;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class Adapter_UserMapper {
    public UserDto toUserDto(User userModel) {
        if(userModel == null){
            return null;
        }
        UserDto userDto =  UserDto.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .username(userModel.getUsername())
                .build();
        if(userModel.getProfile() != null){
            userDto.setProfile(this.toProfileDto(userModel.getProfile()));
        }
        return userDto;
    }

    public ProfileDto toProfileDto(Profile profile) {
        return ProfileDto.builder()
                .dob(profile.getDob())
                .profilePicture(profile.getProfilePicture())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .address(profile.getAddress()
                        .stream()
                        .map(this::toAddressDto)
                        .toList()
                )
                .build();
    }

    public AddressDto toAddressDto(Address address) {
        return AddressDto.builder()
                .phone(address.getPhone())
                .location(address.getLocation())
                .build();
    }
}
