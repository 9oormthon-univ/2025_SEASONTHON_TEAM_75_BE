package com.trashheroesbe.feature.trash.dto.request;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateTrashRequest(
        MultipartFile imageFile
) {
    public void validate() {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일은 필수입니다.");
        }
        if (imageFile.getContentType() == null || !imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
        if (imageFile.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }
    }
}
