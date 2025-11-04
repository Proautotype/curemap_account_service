package com.custard.account_service.adapter.rest;

import com.custard.account_service.adapter.dto.UserDto;
import com.custard.account_service.adapter.dto.reponses.SuccessApiResponse;
import com.custard.account_service.adapter.mapper.Adapter_UserMapper;
import com.custard.account_service.application.commands.CreateProfileCommand;
import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.application.commands.UpdateUserCommand;
import com.custard.account_service.application.usecases.CreateProfileUseCase;
import com.custard.account_service.application.usecases.CreateUserUseCase;
import com.custard.account_service.application.usecases.FindUserByIdUseCase;
import com.custard.account_service.application.usecases.UpdateUserUseCase;
import com.custard.account_service.domain.models.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Tag(name = "UserController", description = "Contains apis to perform crud operation for accounts including user, profile and address")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Adapter_UserMapper adapterUserMapper;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final CreateProfileUseCase createProfileUsecase;
    private final FindUserByIdUseCase getUserUseCase;

    @PostMapping("/create-account")
    public ResponseEntity<SuccessApiResponse<UserDto>> create(@RequestBody CreateUserCommand command) {
        logger.info("Create user request received");
        User user = createUserUseCase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @GetMapping("/get-account/{userId}")
    public ResponseEntity<SuccessApiResponse<UserDto>> getAccountDetails(@PathVariable("userId") String userId) {
        logger.info("get user request received by id : {} ", userId);
        User user = getUserUseCase.execute(userId);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @PatchMapping("/update-account")
    public ResponseEntity<SuccessApiResponse<UserDto>> update(UpdateUserCommand command) {
        logger.info("Update user request received");
        User user = updateUserUseCase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @PatchMapping("/profile")
    public ResponseEntity<SuccessApiResponse<UserDto>> addUserProfile(@RequestBody CreateProfileCommand command) {
        logger.info("create profile user request received");
        User user = createProfileUsecase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<SuccessApiResponse<UserDto>> getProfileByUserId(CreateProfileCommand command) {
        logger.info("create profile user request received");
        User user = createProfileUsecase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

}
