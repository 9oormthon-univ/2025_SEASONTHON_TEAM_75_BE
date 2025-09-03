package com.trashheroesbe.infrastructure.adapter.out.s3;

import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileStorageAdapter implements FileStoragePort {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Override
    public String uploadFile(String key, String contentType, byte[] fileData) {
        log.info("S3 파일 업로드 시작: bucket={}, key={}", bucketName, key);

        PutObjectRequest put = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build();

        s3Client.putObject(put, RequestBody.fromBytes(fileData));

        String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
        log.info("S3 파일 업로드 완료: {}", url);
        return url;
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        log.info("S3 파일 삭제 시작: bucket={}, key={}", bucketName, key);

        DeleteObjectRequest del = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        s3Client.deleteObject(del);
        log.info("S3 파일 삭제 완료: {}", fileUrl);
    }

    private String buildKey(String fileName, String pathPrefix) {
        // fileName에 이미 경로가 포함되어 있으면 그대로 사용
        if (fileName != null && fileName.contains("/")) {
            return fileName;
        }
        String prefix = (pathPrefix == null || pathPrefix.isBlank()) ? "" : pathPrefix.trim();
        return prefix.isEmpty() ? fileName : prefix + "/" + fileName;
    }

    private String extractKeyFromUrl(String fileUrl) {
        try {
            String path = URI.create(fileUrl).getPath();           // /trash/2025.../abc.jpg
            return path.startsWith("/") ? path.substring(1) : path; // trash/2025.../abc.jpg
        } catch (Exception e) {
            // 도메인이 변형된 경우를 위한 보정
            final String marker = ".amazonaws.com/";
            int idx = fileUrl.indexOf(marker);
            if (idx >= 0) {
                return fileUrl.substring(idx + marker.length());
            }
            int slash = fileUrl.indexOf('/', fileUrl.indexOf("://") + 3);
            return (slash >= 0 && slash + 1 < fileUrl.length()) ? fileUrl.substring(slash + 1)
                : fileUrl;
        }
    }
}