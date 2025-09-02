package com.trashheroesbe.feature.gpt.dto.response;

import com.trashheroesbe.feature.trash.domain.TrashType;

public record TrashAnalysisResponseDto(
        TrashType type,
        String item,
        String description
) {
    public static TrashAnalysisResponseDto of(TrashType type, String item, String description) {
        return new TrashAnalysisResponseDto(type, item, description);
    }
}
