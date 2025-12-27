package com.trashheroesbe.feature.point.domain.event;

import com.trashheroesbe.feature.point.domain.type.PointReason;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointEarnedEvent {

    private final Long userId;
    private final Long relatedEntityId;
    private final PointReason reason;
    private final LocalDateTime occurredAt;
}
