package com.trashheroesbe.feature.badge.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.context.BadgeContextHolder;
import com.trashheroesbe.global.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class BadgeEventHandler {

    private final BadgeManager badgeManager;
    private final UserRepository userRepository;

    @Async("badgeTaskExecutor")
    @EventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void handleTrashAnalysisCompleted(TrashAnalysisEvent event) {
        User user = userRepository.findById(event.getUserId())
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        List<UserBadgeResponse> badgeResponses = badgeManager.processBadgeEvent(user, event);

        if (!badgeResponses.isEmpty()) {
            BadgeContextHolder.setNewBadges(badgeResponses);
        }
    }
}