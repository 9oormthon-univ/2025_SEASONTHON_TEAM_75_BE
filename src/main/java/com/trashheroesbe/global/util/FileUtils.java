package com.trashheroesbe.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileUtils {

    /**
     * 저장될 파일명을 생성합니다. 형식: trash/YYYYMMDD_HHmmss_UUID_확장자
     */
    public static String generateStoredFileName(String originalFileName) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        return String.format("trash/%s_%s%s", timestamp, uuid, extension);
    }
}