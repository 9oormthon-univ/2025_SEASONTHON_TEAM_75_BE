package com.trashheroesbe.feature.point.application;

import com.trashheroesbe.feature.point.domain.event.PointEarnedEvent;
import com.trashheroesbe.feature.point.domain.service.PointCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointEventHandler {

    private final PointManager pointManager;
    private final PointCalculator pointCalculator;

    @Async("pointTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePointEarned(PointEarnedEvent event) {
        try {
            Integer randomPoints = pointCalculator.calculateRandomPoints();

            pointManager.processPointEvent(
                event.getUserId(),
                randomPoints,
                event.getReason().getDescription(),
                event.getRelatedEntityId()
            );

            log.info("포인트 지급 성공: userId={}, entityId={}, points={}",
                event.getUserId(), event.getRelatedEntityId(), randomPoints);

        } catch (Exception e) {
            // 재시도 처리 해야 함
            log.error("포인트 지급 실패: userId={}, entityId={}",
                event.getUserId(), event.getRelatedEntityId(), e);
        }
    }
}
