package com.trashheroesbe.feature.trash.dto.response;


import com.trashheroesbe.feature.trash.domain.entity.TrashType;

public record TrashAnalysisResponseDto(
        TrashType type,
        String item
) {
    public static TrashAnalysisResponseDto of(TrashType type, String item) {
        return new TrashAnalysisResponseDto(type, item);
    }
}
