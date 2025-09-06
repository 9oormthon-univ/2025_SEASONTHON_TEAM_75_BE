package com.trashheroesbe.infrastructure.port.gpt;

import com.trashheroesbe.feature.trash.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.infrastructure.adapter.out.gpt.dto.SimilarResult;

public interface ChatAIClientPort {

    TrashAnalysisResponseDto analyzeType(byte[] imageBytes, String contentType);

    String analyzeItem(byte[] imageBytes, String contentType, Type type);

    SimilarResult findSimilarTrashItem(String keyword);

    String suggestNameByImage(byte[] imageBytes, String contentType, Type type);

    String suggestNameByKeyword(String keyword, Type type);
}