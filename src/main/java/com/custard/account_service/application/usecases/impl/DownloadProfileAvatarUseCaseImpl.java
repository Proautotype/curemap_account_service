package com.custard.account_service.application.usecases.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.custard.account_service.application.usecases.DownloadProfileAvatarUseCase;
import com.custard.account_service.domain.models.Profile;
import com.custard.account_service.domain.ports.ProfileRepository;
import com.custard.account_service.infrastructure.config.AppS3Client;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DownloadProfileAvatarUseCaseImpl implements DownloadProfileAvatarUseCase {

    private final AppS3Client client;
    private final ProfileRepository profileRepository;
    private final AppS3Client appS3Client;
    private final Logger logger = LoggerFactory.getLogger(DownloadProfileAvatarUseCaseImpl.class);

    @Override
    public ByteArrayOutputStream execute(String userId) {
        S3Object object = null;
        S3ObjectInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Profile profile = profileRepository.findByUserId(userId);
            logger.info("profile detail: {}", profile);

            String bucket = appS3Client.getBucket();
            String key = profile.getProfilePicture(); // must only be key, no URL

            logger.info("Downloading from S3 - bucket: {}, key: {}", bucket, key);

            object = appS3Client.getClient().getObject(bucket, key);
            logger.info("object received {} ", object.getBucketName());
            inputStream = object.getObjectContent();
            logger.info("after object received : inputstream {} ", object.getKey());
            byte[] buffer = new byte[4096];
            int bytesRead;

            logger.info("received object metadata {} ", object.getObjectMetadata());

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            logger.info("after output stream written {} ", outputStream.size());
            return outputStream;

        } catch (AmazonS3Exception e) {
            logger.error("S3 Error: {}", e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            logger.error("IO Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process file download", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (object != null) {
                    object.close();
                }
            } catch (IOException e) {
                logger.error("Error closing resources: {}", e.getMessage(), e);
            }
        }
    }

}
