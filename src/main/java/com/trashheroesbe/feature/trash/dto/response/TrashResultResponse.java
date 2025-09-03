package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.entity.Trash;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record TrashResultResponse(
        Long id,
        String imageUrl,
        String name,
        String summary,
        String itemName,
        String typeCode,
        String typeName,
        List<String> guideSteps,
        String cautionNote,
        LocalDateTime createdAt
) {
    public static TrashResultResponse from(Trash trash) {
        return TrashResultResponse.builder()
                .id(trash.getId())
                .imageUrl(trash.getImageUrl())
                .name(trash.getName())
                .summary(trash.getSummary())
                .itemName(trash.getTrashItem() != null ? trash.getTrashItem().getName() : null)
                .typeCode(trash.getTrashType() != null ? trash.getTrashType().getType().getTypeCode() : null)
                .typeName(trash.getTrashType() != null ? trash.getTrashType().getType().getNameKo() : null)
                .guideSteps(List.of())
                .cautionNote(null)
                .createdAt(trash.getCreatedAt())
                .build();
    }

    public static TrashResultResponse of(Trash trash, List<String> steps, String cautionNote) {
        return TrashResultResponse.builder()
                .id(trash.getId())
                .imageUrl(trash.getImageUrl())
                .name(trash.getName())
                .summary(trash.getSummary())
                .itemName(trash.getTrashItem() != null ? trash.getTrashItem().getName() : null)
                .typeCode(trash.getTrashType() != null ? trash.getTrashType().getType().getTypeCode() : null)
                .typeName(trash.getTrashType() != null ? trash.getTrashType().getType().getNameKo() : null)
                .guideSteps(steps != null ? steps : List.of())
                .cautionNote(cautionNote)
                .createdAt(trash.getCreatedAt())
                .build();
    }
}