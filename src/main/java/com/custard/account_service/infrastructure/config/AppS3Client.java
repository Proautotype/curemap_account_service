package com.custard.account_service.infrastructure.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
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

        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTPS));

        if (s3Config.getS3().getEndpoint() != null && !s3Config.getS3().getEndpoint().isEmpty() ) {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration
                    = new AwsClientBuilder.EndpointConfiguration(
                    s3Config.getS3().getEndpoint(),
                    s3Config.getRegionAsString()
            );
            amazonS3ClientBuilder.setEndpointConfiguration(endpointConfiguration);
        }else {
            amazonS3ClientBuilder.withRegion(s3Config.getRegionAsString());
        }
        this.s3Client = amazonS3ClientBuilder.build();
    }

    public AmazonS3 getClient() {
        return s3Client;
    }

    public void uploadFile(PutObjectRequest putObjectRequest) {
        this.getClient().putObject(putObjectRequest);
    }

    public String getBucket() {
        return s3Config.getS3().getBucketName();
    }


}
