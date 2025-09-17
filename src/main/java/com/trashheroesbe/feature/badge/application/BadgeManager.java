package com.trashheroesbe.feature.badge.application;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.trashheroesbe.feature.badge.domain.entity.Badge;
import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.badge.domain.policy.BadgePolicy;
import com.trashheroesbe.feature.badge.infrastructure.BadgeProgressRepository;
import com.trashheroesbe.feature.badge.infrastructure.BadgeRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.entity.UserBadge;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import com.trashheroesbe.feature.user.infrastructure.UserBadgeRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BadgeManager {

    private final List<BadgePolicy> policies;
    private final BadgeRepository badgeRepository;
    private final BadgeProgressRepository progressRepository;
    private final UserBadgeRepository userBadgeRepository;

    private final Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));

    @Transactional(propagation = REQUIRES_NEW)
    public List<UserBadgeResponse> processBadgeEvent(User user, TrashAnalysisEvent event) {
        List<UserBadgeResponse> badgeResponses = new ArrayList<>();

        for (BadgePolicy policy : policies) {
            try {
                UserBadgeResponse badgeResponse = processBadgePolicy(user, event, policy);
                if (badgeResponse != null) {
                    badgeResponses.add(badgeResponse);
                }
            } catch (Exception e) {
                log.error("정책 {} 오류 for user {}: {}", policy.name(), user.getId(),
                    e.getMessage(), e);
            }
        }
        return badgeResponses;
    }

    private UserBadgeResponse processBadgePolicy(User user, TrashAnalysisEvent event,
        BadgePolicy policy) {
        System.out.println(policy.name() + " ?????뭥미");
        Badge badge = badgeRepository.findByName(policy.name())
            .orElseGet(() -> badgeRepository.save(Badge.builder()
                .name(policy.name())
                .description(policy.description())
                .ruleType(policy.ruleType())
                .targetValue(policy.targetValue())
                .build()));

        BadgeProgress progress = progressRepository.findByUserAndBadge(user, badge)
            .orElseGet(() -> progressRepository.save(BadgeProgress.builder()
                .user(user).badge(badge)
                .progressCount(0)
                .build()));

        policy.applyProgress(progress, event, clock);
        progressRepository.save(progress);

        if (userBadgeRepository.existsByUserAndBadge(user, badge)) {
            return null;
        }

        if (policy.achieved(progress)) {
            UserBadge userBadge = UserBadge.builder()
                .user(user).badge(badge)
                .earnedAt(LocalDateTime.now(clock))
                .build();
            try {
                log.info("뱃지 '{}' 수여 {}", badge.getName(), user.getId());
                UserBadge savedUserbadge = userBadgeRepository.save(userBadge);
                return UserBadgeResponse.from(savedUserbadge);
            } catch (DataIntegrityViolationException ignore) {
                log.error("뱃지 중복 수여 block");
                return null;
            }
        }
        return null;
    }
}