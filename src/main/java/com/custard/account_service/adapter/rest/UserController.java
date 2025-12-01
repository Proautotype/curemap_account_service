package com.custard.account_service.adapter.rest;

import com.custard.account_service.adapter.dto.AppInfo;
import com.custard.account_service.adapter.dto.ProfileDto;
import com.custard.account_service.adapter.dto.UserDto;
import com.custard.account_service.adapter.dto.reponses.FailureApiResponse;
import com.custard.account_service.adapter.dto.reponses.SuccessApiResponse;
import com.custard.account_service.adapter.mapper.Adapter_UserMapper;
import com.custard.account_service.application.commands.CreateProfileCommand;
import com.custard.account_service.application.commands.UpdateUserCommand;
import com.custard.account_service.application.commands.UploadProfileAvatarCommand;
import com.custard.account_service.application.usecases.*;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.models.User;
import com.custard.account_service.infrastructure.config.AppInfoConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "UserController", description = "Contains apis to perform crud operation for accounts including user, profile and address")
@Slf4j
public class UserController {

//    private final log log = logFactory.getlog(this.getClass().getName());
    private final Adapter_UserMapper adapterUserMapper;

    private final UpdateUserUseCase updateUserUseCase;
    private final CreateProfileUseCase createProfileUsecase;
    private final FindUserByIdUseCase getUserUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final FindProfileByUserIdUseCase findProfileByUserIdUseCase;
    private final UploadProfileAvatarUseCase uploadProfileAvatarUseCase;
    private final DownloadProfileAvatarUseCase profileAvatarUseCase;

    private final AppInfoConfiguration appInfoConfiguration;

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("ping");
    }

    @GetMapping("/version")
    public ResponseEntity<AppInfo> version(){
        AppInfo appInfo = new AppInfo(
                appInfoConfiguration.getMode(),
                appInfoConfiguration.getMessage(),
                appInfoConfiguration.getContactDetails().getName()
        );
        log.info("app info {} ", appInfo);
        return ResponseEntity.ok(appInfo);
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
        log.info("get user request received by id : {} ", userId);
        User user = getUserUseCase.execute(userId);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(user));
        return ResponseEntity.status(200).body(successApiResponse);
    }


    @GetMapping("/users/{email}")
    @Operation(
            method = "GET",
            description = "Get user details by email",
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
    public ResponseEntity<SuccessApiResponse<UserDto>> getAccountDetailsByEmail(@PathVariable("email") String email) {
        log.info("get user  by email : {} ", email);
        User execute = findUserByEmailUseCase.execute(email);
        SuccessApiResponse<UserDto> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(adapterUserMapper.toUserDto(execute));
        return ResponseEntity.status(200).body(successApiResponse);
    }

    @PatchMapping("/update")
    public ResponseEntity<SuccessApiResponse<UserDto>> update(UpdateUserCommand command) {
        log.info("Update user request received");
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
        log.info("create profile user request received");
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
        log.info("Finding user profile by id -> {} ", userId);
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
        log.info("Update user profile picture request received");
        UploadProfileAvatarCommand uploadProfileAvatarCommand = new UploadProfileAvatarCommand(file, userId);
        String execute = uploadProfileAvatarUseCase.execute(uploadProfileAvatarCommand);
        SuccessApiResponse<String> successApiResponse = new SuccessApiResponse<>();
        successApiResponse.setData(execute);
        return ResponseEntity.status(201).body(successApiResponse);
    }

    @GetMapping(value = "/profile/avatar/{userId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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
    public ResponseEntity<byte[]> getProfileAvatar(@PathVariable("userId") String userId) {
        try {
            ByteArrayOutputStream outputStream = profileAvatarUseCase.execute(userId);
            byte[] fileContent = outputStream.toByteArray();

            // Get content type from S3 metadata or use a default
            String contentType = "image/jpeg"; // Default content type

            // Set appropriate headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(fileContent.length);
            headers.setContentDispositionFormData("attachment", "profile-avatar");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error downloading profile avatar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
