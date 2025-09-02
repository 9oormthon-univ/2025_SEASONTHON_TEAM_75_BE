package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.Trash;
import com.trashheroesbe.feature.trash.domain.TrashDescription;
import com.trashheroesbe.feature.trash.domain.TrashType;
import com.trashheroesbe.feature.trash.domain.Type;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TrashResult(
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
    public static TrashResult from(Trash trash) {
        TrashType tt = trash.getTrashType();
        Type t = (tt != null) ? tt.getType() : null;
        return TrashResult.builder()
                .id(trash.getId())
                .imageUrl(trash.getImageUrl())
                .name(trash.getName())
                .summary(trash.getSummary())
                .itemName(trash.getTrashItem() != null ? trash.getTrashItem().getName() : null)
                .typeName(t != null ? t.getNameKo() : null)
                .typeCode(t != null ? t.getTypeCode() : null)
                .guideSteps(null)
                .cautionNote(null)
                .createdAt(trash.getCreatedAt())
                .build();
    }
    public static TrashResult of(Trash trash, TrashDescription desc) {
        TrashResult base = from(trash);
        return TrashResult.builder()
                .id(base.id)
                .imageUrl(base.imageUrl)
                .name(base.name)
                .summary(base.summary)
                .itemName(base.itemName)
                .typeCode(base.typeCode)
                .typeName(base.typeName)
                .guideSteps(desc != null ? desc.steps() : null)
                .cautionNote(desc != null ? desc.getCautionNote() : null)
                .createdAt(base.createdAt)
                .build();
    }
}