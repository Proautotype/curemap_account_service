package com.custard.account_service.adapter.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressDto {
    private String phone;
    private String location;
}
