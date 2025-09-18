package com.trashheroesbe.feature.badge.domain.policy;

import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.badge.domain.type.RuleType;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class EcoWarriorPolicy implements BadgePolicy {

    @Override
    public String name() {
        return "친환경전사";
    }

    @Override
    public String description() {
        return "10번 이상 사진분석";
    }

    @Override
    public RuleType ruleType() {
        return RuleType.TOTAL_COUNT;
    }

    @Override
    public int targetValue() {
        return 10;
    }

    @Override
    public void applyProgress(BadgeProgress progress, TrashAnalysisEvent event, Clock clock) {
        progress.incrementCount();
    }

    @Override
    public boolean achieved(BadgeProgress progress) {
        return progress.getProgressCount() != null && progress.getProgressCount() >= targetValue();
    }
}
