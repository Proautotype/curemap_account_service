package com.custard.account_service.application.mapper;

import com.custard.account_service.application.commands.CreateProfileCommand;
import com.custard.account_service.domain.models.Profile;
import org.springframework.stereotype.Component;

@Component
public class Application_UserMapper {

    public Profile toProfile(CreateProfileCommand command){
        return Profile.builder()
                .userId(command.getUserId())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .profilePicture(command.getProfilePicture())
                .build();
    }



}
