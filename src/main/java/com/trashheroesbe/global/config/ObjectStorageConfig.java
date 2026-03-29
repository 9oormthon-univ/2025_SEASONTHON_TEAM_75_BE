package com.trashheroesbe.global.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class ObjectStorageConfig {

    @Bean
    public S3Client s3Client(
        @Value("${storage.s3.endpoint}") String endpoint,
        @Value("${storage.s3.region}") String region,
        @Value("${storage.s3.access-key}") String accessKey,
        @Value("${storage.s3.secret-key}") String secretKey,
        @Value("${storage.s3.path-style-access:true}") boolean pathStyleAccess,
        @Value("${storage.s3.checksum-validation-enabled:false}") boolean checksumValidationEnabled
    ) {
        S3ClientBuilder builder = S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(pathStyleAccess)
                    .chunkedEncodingEnabled(false)
                    .build()
            );

        if (checksumValidationEnabled) {
            builder.requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED);
            builder.responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED);
        }

        return builder.build();
    }
}
