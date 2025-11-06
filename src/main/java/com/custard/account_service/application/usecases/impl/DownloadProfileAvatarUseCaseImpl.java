package com.custard.account_service.application.usecases.impl;

import com.amazonaws.AmazonClientException;
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
    public ByteArrayOutputStream execute(String command) {
        try {
            Profile profile = profileRepository.findByUserId(command);

            logger.info("profile detail \n {}", profile);

            String bucket = appS3Client.getBucket();
            S3Object pfAvatarObject = appS3Client.getClient().getObject(bucket, profile.getProfilePicture());
            S3ObjectInputStream objectContent = pfAvatarObject.getObjectContent();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = objectContent.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream;
        } catch (IOException e) {
            logger.error(e.getMessage(), e, e);
            throw new RuntimeException(e);
        } catch (AmazonClientException ace) {
            logger.error(ace.getMessage(), ace, ace);
            throw new AmazonClientException(ace);
        }
    }

}
