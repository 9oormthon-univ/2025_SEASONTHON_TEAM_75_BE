package com.trashheroesbe.feature.point.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;
import static com.trashheroesbe.global.response.type.ErrorCode.INSUFFICIENT_POINTS;

import com.trashheroesbe.feature.point.domain.entity.UserPoint;
import com.trashheroesbe.feature.point.domain.entity.UserPointHistory;
import com.trashheroesbe.feature.point.domain.event.PointEarnedEvent;
import com.trashheroesbe.feature.point.domain.service.PointCalculator;
import com.trashheroesbe.feature.point.domain.type.ActionType;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import com.trashheroesbe.feature.point.dto.response.PointEarnedResult;
import com.trashheroesbe.feature.point.dto.response.UserPointResponse;
import com.trashheroesbe.feature.point.infrastructure.UserPointHistoryRepository;
import com.trashheroesbe.feature.point.infrastructure.UserPointRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointRepository userPointRepository;
    private final UserPointHistoryRepository userPointHistoryRepository;

    private final UserFinder userFinder;
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

    @Transactional
    public UserPointResponse getMyPoint(User user) {
        UserPoint userPoint = userPointRepository.findByUserIdWithLock(user.getId())
            .orElseGet(() -> {
                try {
                    UserPoint newPoint = UserPoint.createInit(user);
                    return userPointRepository.save(newPoint);
                } catch (DataIntegrityViolationException e) {
                    log.info("UserPoint가 다른 트랜잭션에서 생성 중 -> 재시도");
                    return userPointRepository.findByUserIdWithLock(user.getId())
                        .orElseThrow(() -> new IllegalStateException(
                            "UserPoint 생성 실패 - userId: " + user.getId()
                        ));
                }
            });

        return UserPointResponse.from(userPoint);
    }

    @Transactional
    @Retryable(
        retryFor = ObjectOptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public void usePoint(
        Long userId,
        Integer points,
        PointReason reason,
        Long relatedEntityId
    ) {
        User user = userFinder.findById(userId);

        UserPoint userPoint = userPointRepository.findByUserIdWithLock(userId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        userPoint.usePoints(points);
        userPointRepository.save(userPoint);

        UserPointHistory userPointHistory = UserPointHistory.create(
            user,
            points,
            userPoint.getTotalPoint(),
            ActionType.USED,
            reason,
            relatedEntityId
        );

        userPointHistoryRepository.save(userPointHistory);

        log.info("포인트 사용 완료 - userId: {}, amount: {}, balanceAfter: {}, reason: {}",
            userId, points, userPoint.getTotalPoint(), reason);
    }
}
