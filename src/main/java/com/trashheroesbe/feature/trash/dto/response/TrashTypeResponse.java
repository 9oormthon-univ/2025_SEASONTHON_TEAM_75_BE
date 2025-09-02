package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import lombok.Builder;

@Builder
public record TrashTypeResponse(
    Long trashTypeId,
    String typeCode,
    String typeName
) {

    public static TrashTypeResponse from(TrashType trashType) {
        return TrashTypeResponse.builder()
            .trashTypeId(trashType.getId())
            .typeCode(trashType.getType().getTypeCode())
            .typeName(trashType.getType().getNameKo())
            .build();
    }
}
