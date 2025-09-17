package com.trashheroesbe.feature.badge.domain.policy;

import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.badge.domain.type.RuleType;
import java.time.Clock;

public interface BadgePolicy {

    String name();

    String description();

    RuleType ruleType();

    int targetValue();

    void applyProgress(BadgeProgress progress, TrashAnalysisEvent event, Clock clock);

    boolean achieved(BadgeProgress progress);
}
