package com.trashheroesbe.infrastructure.adapter.out.s3;

import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileAdapter implements FileStoragePort {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Override
    public String uploadFile(
        String fileName,
        String pathPrefix,
        String contentType,
        byte[] fileData
    ) {
        log.info("S3 파일 업로드 시작: bucket={}, fileName={}", bucketName, fileName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));

            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, fileName);

            log.info("S3 파일 업로드 완료: {}", fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("S3 파일 업로드 실패: {}", fileName, e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    @Override
    public void deleteFileByUrl(final String fileUrl) {

    }
}