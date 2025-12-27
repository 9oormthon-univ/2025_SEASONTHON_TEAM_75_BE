package com.trashheroesbe.feature.point.application;

import com.trashheroesbe.feature.point.domain.event.PointEarnedEvent;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final ApplicationEventPublisher publisher;

    public void grantPointsForTrash(Long userId, Long trashId) {
        PointEarnedEvent event = PointEarnedEvent.builder()
            .userId(userId)
            .relatedEntityId(trashId)
            .reason(PointReason.TRASH_CREATED)
            .occurredAt(LocalDateTime.now())
            .build();

        publisher.publishEvent(event);
    }
}
