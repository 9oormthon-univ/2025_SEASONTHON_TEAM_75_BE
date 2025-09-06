package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.type.Type;

public record PartCardResponse(
        String name,
        String typeCode,
        String typeName
) {
    public static PartCardResponse of(String name, Type type) {
        return new PartCardResponse(name, type.getTypeCode(), type.getNameKo());
    }
}