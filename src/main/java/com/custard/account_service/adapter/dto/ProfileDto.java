package com.custard.account_service.adapter.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileDto {
    private String userId;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private Date dob;
    private List<AddressDto> address;
}
