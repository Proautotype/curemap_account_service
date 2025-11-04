package com.custard.account_service.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String userId;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private Date dob;
    private List<Address> address;
}
