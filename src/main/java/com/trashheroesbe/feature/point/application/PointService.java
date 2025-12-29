package com.trashheroesbe.feature.point.application;

import com.trashheroesbe.feature.point.domain.event.PointEarnedEvent;
import com.trashheroesbe.feature.point.domain.service.PointCalculator;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import com.trashheroesbe.feature.point.dto.response.PointEarnedResult;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final ApplicationEventPublisher publisher;
    private final PointCalculator pointCalculator;

    public PointEarnedResult grantPointsForTrash(Long userId, Long trashId) {
        Integer earnedPoints = pointCalculator.calculateRandomPoints();
        LocalDateTime now = LocalDateTime.now();

        PointEarnedEvent event = PointEarnedEvent.builder()
            .userId(userId)
            .relatedEntityId(trashId)
            .reason(PointReason.TRASH_CREATED)
            .earnedPoints(earnedPoints)
            .occurredAt(now)
            .build();

        publisher.publishEvent(event);
        return new PointEarnedResult(earnedPoints, now);
    }
}
