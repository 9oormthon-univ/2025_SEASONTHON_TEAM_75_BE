package com.trashheroesbe.infrastructure.port.gpt;

import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.infrastructure.adapter.out.gpt.dto.SimilarResult;

import java.util.List;

public interface ChatAIClientPort {

    TrashAnalysisResponseDto analyzeType(byte[] imageBytes, String contentType);

    String analyzeItem(byte[] imageBytes, String contentType, Type type);

    SimilarResult findSimilarTrashItem(String keyword, List<String> itemNames, List<Type> types);

    String suggestNameByImage(byte[] imageBytes, String contentType, Type type);

    String suggestNameByKeyword(String keyword, Type type);

    ImageAnalysisBundle analyzeAll(byte[] imageBytes, String contentType);
}