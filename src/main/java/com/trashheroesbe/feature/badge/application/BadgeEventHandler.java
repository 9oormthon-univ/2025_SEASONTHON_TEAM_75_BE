package com.trashheroesbe.feature.badge.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.context.BadgeContextHolder;
import com.trashheroesbe.global.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@RequiredArgsConstructor
public class BadgeEventHandler {

    private final BadgeManager badgeManager;
    private final UserRepository userRepository;

    @EventListener
    public void handleTrashAnalysisCompleted(TrashAnalysisEvent event) {
        User user = userRepository.findById(event.getUserId())
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        List<UserBadgeResponse> badgeResponses = badgeManager.processBadgeEvent(user, event);

        // 새로 획득한 뱃지가 있으면 ThreadLocal에 저장
        if (!badgeResponses.isEmpty()) {
            BadgeContextHolder.setNewBadges(badgeResponses);
        }
    }
}