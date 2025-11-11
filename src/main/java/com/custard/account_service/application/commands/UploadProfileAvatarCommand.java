package com.custard.account_service.application.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadProfileAvatarCommand {
    private MultipartFile avatarFile;
    private String userId;
}
