package com.trashheroesbe.infrastructure.port.s3;

public interface FileStoragePort {

    /**
     * 파일을 저장소에 업로드합니다.
     *
     * @param fileName    저장할 파일명
     * @param contentType 파일 콘텐츠 타입
     * @param fileData    파일 데이터
     * @return 업로드된 파일의 URL
     */
    String uploadFile(
        String fileName,
        String pathPrefix,
        String contentType,
        byte[] fileData
    );
}