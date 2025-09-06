package com.trashheroesbe.infrastructure.adapter.out.gpt.dto;

import com.trashheroesbe.feature.trash.domain.type.Type;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SimilarResult {

    private final String itemName;
    private final Type type;

    public static SimilarResult ofItem(String itemName) {
        return new SimilarResult(itemName, null);
    }

    public static SimilarResult ofType(Type type) {
        return new SimilarResult(null, type);
    }

    public static SimilarResult none() {
        return new SimilarResult(null, null);
    }

    public Optional<String> getItemName() {
        return Optional.ofNullable(itemName);
    }

    public Optional<Type> getType() {
        return Optional.ofNullable(type);
    }

    public boolean isNone() {
        return itemName == null && type == null;
    }
}
