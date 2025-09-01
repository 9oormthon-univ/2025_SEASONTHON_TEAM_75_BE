package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.Trash;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record TrashResult(
        Long id,
        String imageUrl,
        String name,
        LocalDateTime createdAt
) {
    public static TrashResult from(Trash trash) {
        return TrashResult.builder()
                .id(trash.getId())
                .imageUrl(trash.getImageUrl())
                .name(trash.getName())
                .createdAt(trash.getCreatedAt())
                .build();
    }
}