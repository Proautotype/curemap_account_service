package com.custard.account_service.application.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserCommand {
    private String id;
    private String username;
    private String email;
    private String password;
}
