package com.trashheroesbe.infrastructure.port.gpt;

import com.trashheroesbe.feature.trash.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.type.Type;

public interface ChatAIClientPort {

    TrashAnalysisResponseDto analyzeType(byte[] imageBytes, String contentType);

    String analyzeItem(byte[] imageBytes, String contentType, Type type);

    Type findSimilarTrashItem(String keyword);
}