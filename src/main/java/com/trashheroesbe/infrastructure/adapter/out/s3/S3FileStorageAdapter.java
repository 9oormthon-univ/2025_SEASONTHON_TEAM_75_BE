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

    @Value("${storage.s3.bucket}")
    private String bucketName;

    @Value("${storage.s3.public-base-url}")
    private String publicBaseUrl;

    @Override
    public String uploadFile(String key, String contentType, byte[] fileData) {
        log.info("S3 파일 업로드 시작: bucket={}, key={}", bucketName, key);

        PutObjectRequest put = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build();

        s3Client.putObject(put, RequestBody.fromBytes(fileData));

        String url = String.format("%s/%s", trimTrailingSlash(publicBaseUrl), key);
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
        String normalizedBaseUrl = trimTrailingSlash(publicBaseUrl);
        if (fileUrl != null && fileUrl.startsWith(normalizedBaseUrl + "/")) {
            return fileUrl.substring(normalizedBaseUrl.length() + 1);
        }

        try {
            String path = URI.create(fileUrl).getPath();
            String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
            String bucketPrefix = bucketName + "/";
            return normalizedPath.startsWith(bucketPrefix)
                ? normalizedPath.substring(bucketPrefix.length())
                : normalizedPath;
        } catch (Exception e) {
            int slash = fileUrl.indexOf('/', fileUrl.indexOf("://") + 3);
            String fallback = (slash >= 0 && slash + 1 < fileUrl.length())
                ? fileUrl.substring(slash + 1)
                : fileUrl;
            String bucketPrefix = bucketName + "/";
            return fallback.startsWith(bucketPrefix)
                ? fallback.substring(bucketPrefix.length())
                : fallback;
        }
    }

    private String trimTrailingSlash(String value) {
        return value != null && value.endsWith("/")
            ? value.substring(0, value.length() - 1)
            : value;
    }
}
