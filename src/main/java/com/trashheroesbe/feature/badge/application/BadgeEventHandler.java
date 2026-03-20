package com.trashheroesbe.feature.badge.application;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class BadgeEventHandler {

    private final BadgeManager badgeManager;
    private final UserFinder userFinder;

    @Async("badgeTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void handleTrashAnalysisCompleted(TrashAnalysisEvent event) {
        User user = userFinder.findById(event.getUserId());
        List<UserBadgeResponse> badgeResponses = badgeManager.processBadgeEvent(user, event);
    }
}