package com.trashheroesbe.feature.point.application;

import com.trashheroesbe.feature.point.domain.entity.UserPoint;
import com.trashheroesbe.feature.point.domain.entity.UserPointHistory;
import com.trashheroesbe.feature.point.domain.type.ActionType;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import com.trashheroesbe.feature.point.infrastructure.UserPointHistoryRepository;
import com.trashheroesbe.feature.point.infrastructure.UserPointRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointManager {

    private final UserFinder userFinder;
    private final UserPointRepository userPointRepository;
    private final UserPointHistoryRepository userPointHistoryRepository;

    @Transactional
    @Retryable(
        retryFor = ObjectOptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public void processEarnPointEvent(
        Long userId,
        Integer randomPoints,
        PointReason reason,
        Long relatedEntityId
    ) {
        User user = userFinder.findById(userId);

        UserPoint userPoint = userPointRepository.findByUserIdWithLock(userId)
            .orElseGet(() -> {
                UserPoint newPoint = UserPoint.createInit(user);
                return userPointRepository.save(newPoint);
            });

        userPoint.earnPoints(randomPoints);
        userPointRepository.save(userPoint);

        UserPointHistory userPointHistory = UserPointHistory.create(
            user,
            randomPoints,
            userPoint.getTotalPoint(),
            ActionType.EARNED,
            reason,
            relatedEntityId
        );

        userPointHistoryRepository.save(userPointHistory);

        log.info("포인트 적립 완료 - userId: {}, amount: {}, balanceAfter: {}, reason: {}",
            userId, randomPoints, userPoint.getTotalPoint(), reason);
    }
}
