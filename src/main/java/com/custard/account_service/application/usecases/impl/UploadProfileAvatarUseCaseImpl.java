package com.custard.account_service.application.usecases.impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.custard.account_service.application.commands.UploadProfileAvatarCommand;
import com.custard.account_service.application.exceptions.InaccurateInputException;
import com.custard.account_service.application.usecases.UploadProfileAvatarUseCase;
import com.custard.account_service.domain.ports.ProfileRepository;
import com.custard.account_service.infrastructure.config.AppS3Client;
import com.custard.account_service.infrastructure.config.FileUploadConfig;
import com.custard.account_service.infrastructure.config.S3Config;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadProfileAvatarUseCaseImpl implements UploadProfileAvatarUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UploadProfileAvatarUseCaseImpl.class.getName());
    private final FileUploadConfig fileUploadConfig;
    private final AppS3Client appS3Client;
    private final S3Config s3Config;
    private final ProfileRepository profileRepository;


    @PostConstruct
    private void printS3Config() {
        logger.info("S3 Info {} ", s3Config);
    }


    @Override
    public String execute(UploadProfileAvatarCommand command) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();

            // 1. Check if the file is empty
            if (command.getAvatarFile().isEmpty()) {
                throw new InaccurateInputException("Empty file not acceptable");
            }

            String originalFilename = command.getAvatarFile().getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new InaccurateInputException("Invalid file name");
            }

            String fileExtension = getFileExtension(originalFilename).toLowerCase();
            if (!isValidImageExtension(fileExtension)) {
                throw new InaccurateInputException("Invalid file type. Allowed types: " +
                        String.join(", ", fileUploadConfig.getAcceptedImages()));
            }

            String contentType = command.getAvatarFile().getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Invalid file content type");
            }
            objectMetadata.setContentType(contentType);

            long maxSize = fileUploadConfig.getFileLimits().getMaxImageSizeMB();
            logger.info("File size -> {} ", command.getAvatarFile().getSize());
            if (command.getAvatarFile().getSize() > maxSize) {
                throw new IllegalArgumentException("File size exceeds maximum limit of " +
                        fileUploadConfig.getFileLimits().getMaxImageSizeMB() + "MB");
            }
            objectMetadata.setContentLength(command.getAvatarFile().getSize());

            // 6. Generate unique filename
            String uniqueFilename = UUID.randomUUID() + fileExtension;
            logger.info("Processing file upload: {} as {}", originalFilename, uniqueFilename);

            String objectKeyName = "avatar".concat("_").concat(command.getUserId().concat("_").concat(uniqueFilename));
            objectKeyName = "profileAvatars.".concat(objectKeyName);

//            appS3Client.getClient().listBuckets().forEach(bucket -> {
//                logger.info("Bucket {} ", bucket.getName());
//            });

            appS3Client.getClient().putObject(s3Config.getBucketName(), objectKeyName, command.getAvatarFile().getInputStream(), objectMetadata);
            //  we assume file already exist
            // update user profile with keyName
            profileRepository.updateProfileAvatar(objectKeyName, command.getUserId());

            return objectKeyName;
        } catch (Exception e) {
            logger.error(e.getMessage(), e, e);
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return filename.substring(lastDot).toLowerCase();
    }

    private boolean isValidImageExtension(String extension) {
        return fileUploadConfig.getAcceptedImages().contains(extension.toLowerCase());
    }
}
