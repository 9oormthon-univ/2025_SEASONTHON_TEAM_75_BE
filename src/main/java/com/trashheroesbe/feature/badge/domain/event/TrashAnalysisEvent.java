package com.trashheroesbe.feature.badge.domain.event;

import com.trashheroesbe.feature.trash.domain.type.Type;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashAnalysisEvent {

    private final Long userId;
    private final Type trashType;
    private final LocalDateTime occurredAt;
}
