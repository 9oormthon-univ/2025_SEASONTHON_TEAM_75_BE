package com.trashheroesbe.feature.point.application;

import com.trashheroesbe.feature.point.domain.entity.UserPoint;
import com.trashheroesbe.feature.point.domain.event.PointEarnedEvent;
import com.trashheroesbe.feature.point.domain.service.PointCalculator;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import com.trashheroesbe.feature.point.dto.response.PointEarnedResult;
import com.trashheroesbe.feature.point.dto.response.UserPointResponse;
import com.trashheroesbe.feature.point.infrastructure.UserPointRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointRepository userPointRepository;

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

    @Transactional(readOnly = true)
    public UserPointResponse getMyPoint(User user) {
        UserPoint userPoint = userPointRepository.findByUserIdWithLock(user.getId())
            .orElseGet(() -> {
                UserPoint newUserPoint = UserPoint.createInit(user);
                return userPointRepository.save(newUserPoint);
            });

        return UserPointResponse.from(userPoint);
    }
}
