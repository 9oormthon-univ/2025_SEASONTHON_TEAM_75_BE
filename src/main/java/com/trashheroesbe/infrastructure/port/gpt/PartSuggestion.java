package com.trashheroesbe.infrastructure.port.gpt;

import com.trashheroesbe.feature.trash.domain.type.Type;

public record PartSuggestion(String name, Type type) {
}