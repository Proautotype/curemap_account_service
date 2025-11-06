package com.custard.account_service.adapter.rest;

import com.custard.account_service.adapter.dto.ProfileDto;
import com.custard.account_service.adapter.dto.UserDto;
import com.custard.account_service.adapter.dto.reponses.FailureApiResponse;
import com.custard.account_service.adapter.dto.reponses.SuccessApiResponse;
import com.custard.account_service.adapter.mapper.Adapter_UserMapper;
import com.custard.account_service.application.commands.CreateProfileCommand;
import com.custard.account_service.application.commands.CreateUserCommand;
import com.custard.account_service.application.commands.UpdateUserCommand;
import com.custard.account_service.application.commands.UploadProfileAvatarCommand;
import com.custard.account_service.application.usecases.*;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

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
    private final FindProfileByUserIdUseCase findProfileByUserIdUseCase;
    private final UploadProfileAvatarUseCase uploadProfileAvatarUseCase;
    private final DownloadProfileAvatarUseCase profileAvatarUseCase;

    @PostMapping("/create")
    /**
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

    @GetMapping("/{userId}")
    @Operation(
            method = "GET",
            description = "Get user details by userId",
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
                    )
            }
    )
    public ResponseEntity<SuccessApiResponse<UserDto>> getAccountDetails(@PathVariable("userId") String userId) {
        logger.info("get user request received by id : {} ", userId);
        User user = getUserUseCase.execute(userId);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @PatchMapping("/update")
    public ResponseEntity<SuccessApiResponse<UserDto>> update(UpdateUserCommand command) {
        logger.info("Update user request received");
        User user = updateUserUseCase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @PatchMapping("/profile")
    @Operation(
            method = "PATCH",
            description = "Add user profile",
            responses = {
                    @ApiResponse(
                            description = "Success with code 00",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "User not found with code 01",
                            responseCode = "404",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = FailureApiResponse.class)
                                    )
                            }
                    )
            }
    )
    public ResponseEntity<SuccessApiResponse<UserDto>> addUserProfile(@RequestBody CreateProfileCommand command) {
        logger.info("create profile user request received");
        User user = createProfileUsecase.execute(command);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @GetMapping("/profile/{userId}")
    @Operation(
            method = "GET",
            description = "Get user profile by user id",
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
                    )
            }
    )
    public ResponseEntity<SuccessApiResponse<ProfileDto>> getProfileByUserId(@PathVariable("userId") String userId) {
        logger.info("Finding user profile by id -> {} ", userId);
        Profile user = findProfileByUserIdUseCase.execute(userId);
        SuccessApiResponse<ProfileDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toProfileDto(user));
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @PostMapping(value = "/profile-picture", consumes = "multipart/form-data")
    @Operation(
            method = "POST",
            description = "Update user profile picture",
            responses = {
                    @ApiResponse(
                            description = "Success with code 00",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "User not found with code 01",
                            responseCode = "404",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = FailureApiResponse.class)
                                    )
                            }
                    )
            }
    )
    public ResponseEntity<SuccessApiResponse<String>> updateProfilePicture(
            @RequestPart("file") MultipartFile file,
            @RequestParam("userId") String userId
    ) {
        logger.info("Update user profile picture request received");
        UploadProfileAvatarCommand uploadProfileAvatarCommand = new UploadProfileAvatarCommand(file, userId);
        String execute = uploadProfileAvatarUseCase.execute(uploadProfileAvatarCommand);
        SuccessApiResponse<String> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(execute);
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @GetMapping(value = "/profile/{userId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            method = "GET",
            description = "Get user profile avatar by user id",
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
                    )
            }
    )
    public ResponseEntity<ByteArrayOutputStream> getProfileAvatar(@PathVariable("userId") String userId) {
        ByteArrayOutputStream execute = profileAvatarUseCase.execute(userId);
        return ResponseEntity.ok(execute);
    }

}
