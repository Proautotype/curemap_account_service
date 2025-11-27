package com.custard.account_service.adapter.rest;

import com.custard.account_service.adapter.dto.UserDto;
import com.custard.account_service.adapter.dto.reponses.FailureApiResponse;
import com.custard.account_service.adapter.dto.reponses.SuccessApiResponse;
import com.custard.account_service.adapter.mapper.Adapter_UserMapper;
import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.application.commands.LoginUserCommand;
import com.custard.account_service.application.usecases.CreateUserUseCase;
import com.custard.account_service.application.usecases.LoginUseCase;
import com.custard.account_service.application.usecases.RefreshTokenUseCase;
import com.custard.account_service.domain.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Contains apis to perform crud operation for authentication")

public class AuthenticationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final Adapter_UserMapper adapterUserMapper;

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("ping");
    }

    @PostMapping("/create")
    /*
     * Creates a new user.
     * @param command the create user command containing username and email.
     * @return a response entity containing the created user.
     */
    @Operation(
            method = "POST",
            description = "Create a new user on the AccountService",
            responses = {
                    @ApiResponse(
                            description = "Success with code 00",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "User already exist with code 01",
                            responseCode = "402"
                    ),
                    @ApiResponse(
                            description = "Internal Server error with code 01",
                            responseCode = "500",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = FailureApiResponse.class)
                                    )
                            }
                    )
            }
    )
    public ResponseEntity<SuccessApiResponse<UserDto>> create(@RequestBody CreateUserCommand command) {
        logger.info("Create user request received");
        User user = createUserUseCase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @PostMapping(value = "/login")
    @Operation(
            method = "POST",
            description = "Login to the AccountService",
            responses = {
                    @ApiResponse(
                            description = "Success with code 00",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "User not found with code 01",
                            responseCode = "404",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = FailureApiResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "Internal Server error with code 01",
                            responseCode = "500",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = FailureApiResponse.class)
                                    )
                            }
                    )
            }
    )
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginUserCommand command){
        logger.info("Login request received {} ", command);
        Map<String, Object> execute = loginUseCase.execute(command);
        return ResponseEntity.ok(execute);
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<Map<String,Object>> refreshToken(@RequestParam("token") String refreshToken){
        logger.info("refresh token request received {} ", refreshToken);
        Map<String, Object> execute = refreshTokenUseCase.execute(refreshToken);
        return ResponseEntity.ok(execute);
    }

}
