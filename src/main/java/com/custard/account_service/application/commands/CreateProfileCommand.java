package com.custard.account_service.application.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProfileCommand {
    private String userId;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private Date dob;
}
