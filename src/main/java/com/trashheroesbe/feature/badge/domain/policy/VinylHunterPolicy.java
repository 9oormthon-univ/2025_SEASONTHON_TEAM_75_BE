package com.trashheroesbe.feature.badge.domain.policy;

import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.badge.domain.type.RuleType;
import com.trashheroesbe.feature.trash.domain.type.Type;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class VinylHunterPolicy implements BadgePolicy {

    @Override
    public String name() {
        return "비닐사냥꾼";
    }

    @Override
    public String description() {
        return "비닐 3번 이상 사진분석";
    }

    @Override
    public RuleType ruleType() {
        return RuleType.TYPE_COUNT;
    }

    @Override
    public int targetValue() {
        return 3;
    }

    @Override
    public void applyProgress(
        BadgeProgress progress,
        TrashAnalysisEvent event,
        Clock clock
    ) {
        if (event.getTrashType() == Type.VINYL_FILM) {
            progress.incrementCount();
        }
    }

    @Override
    public boolean achieved(BadgeProgress progress) {
        return progress.getProgressCount() != null && progress.getProgressCount() >= targetValue();
    }
}
