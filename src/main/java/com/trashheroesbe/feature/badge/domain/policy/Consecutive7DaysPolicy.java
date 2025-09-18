package com.trashheroesbe.feature.badge.domain.policy;

import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.badge.domain.type.RuleType;
import com.trashheroesbe.feature.trash.infrastructure.TrashRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consecutive7DaysPolicy implements BadgePolicy {

    private final TrashRepository trashRepository;

    @Override
    public String name() {
        return "연속7일";
    }

    @Override
    public String description() {
        return "연속 7일 사진분석";
    }

    @Override
    public RuleType ruleType() {
        return RuleType.STREAK;
    }

    @Override
    public int targetValue() {
        return 7;
    }

    @Override
    public void applyProgress(
        BadgeProgress progress,
        TrashAnalysisEvent event,
        Clock clock
    ) {

    }

    @Override
    public boolean achieved(BadgeProgress progress) {
        User user = progress.getUser();
        LocalDate today = LocalDate.now();

        Set<LocalDate> analysisDates = trashRepository
            .findByUserAndCreatedAtBetween(
                user,
                today.minusDays(targetValue() - 1).atStartOfDay(),
                today.plusDays(1).atStartOfDay()
            )
            .stream()
            .map(trash -> trash.getCreatedAt().toLocalDate())
            .collect(Collectors.toSet());

        for (int i = 0; i < targetValue(); i++) {
            LocalDate checkDate = today.minusDays(i);
            if (!analysisDates.contains(checkDate)) {
                return false;
            }
        }

        return true;
    }

}
