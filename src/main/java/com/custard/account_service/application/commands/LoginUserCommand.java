package com.custard.account_service.application.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserCommand {
    @Email
    private String email;
    @NotNull
    private String password;
}
