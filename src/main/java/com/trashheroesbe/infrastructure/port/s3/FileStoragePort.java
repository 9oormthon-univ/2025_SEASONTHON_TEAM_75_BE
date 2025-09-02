package com.trashheroesbe.infrastructure.port.s3;

public interface FileStoragePort {
    String uploadFile(String fileName, String pathPrefix, String contentType, byte[] fileData);
    void deleteFileByUrl(String fileUrl);
}