package com.custard.account_service.infrastructure.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppS3Client {
    private final S3Config s3Config;
    private AmazonS3 s3Client;

    @PostConstruct
    private void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(
                s3Config.getAccessKeyId(),
                s3Config.getSecretAccessKey()
        );

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(s3Config.getRegion())    // ✅ Must match bucket region e.g. "us-east-1"
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTPS))
                .build();
    }

    public AmazonS3 getClient() {
        return s3Client;
    }

    public void uploadFile(PutObjectRequest putObjectRequest){
        this.getClient().putObject(putObjectRequest);
    }

    public String getBucket(){
       return s3Config.getBucketName();
    }


}
