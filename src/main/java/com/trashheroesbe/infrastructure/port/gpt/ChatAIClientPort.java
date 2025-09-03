package com.trashheroesbe.infrastructure.port.gpt;

import com.trashheroesbe.feature.gpt.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.Type;

public interface ChatAIClientPort {
    TrashAnalysisResponseDto analyzeType(byte[] imageBytes, String contentType);
    String analyzeItem(byte[] imageBytes, String contentType, Type type);
}