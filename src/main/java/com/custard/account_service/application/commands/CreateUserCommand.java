package com.custard.account_service.application.commands;

import com.custard.account_service.domain.models.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserCommand {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
