package com.custard.account_service.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppInfoDto {
    private String mode;
    private String message;
    private String contactDetails;
}
