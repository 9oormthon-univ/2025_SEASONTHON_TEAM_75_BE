package com.trashheroesbe.infrastructure.port.gpt;

import com.trashheroesbe.feature.trash.domain.type.Type;

public record ImageAnalysisBundle(
        Type type,
        String itemName,   // DB 목록 중에서만 선택되도록 프롬프트 강제
        String name       // 사용자 친화적 요약명
) {}