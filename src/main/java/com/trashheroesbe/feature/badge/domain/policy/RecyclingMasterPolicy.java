package com.trashheroesbe.feature.badge.domain.policy;


import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.badge.domain.type.RuleType;
import com.trashheroesbe.feature.trash.domain.type.Type;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class RecyclingMasterPolicy implements BadgePolicy {


    @Override
    public String name() {
        return "재활용 마스터";
    }

    @Override
    public String description() {
        return "모든 타입 한번씩 사진분석";
    }

    @Override
    public RuleType ruleType() {
        return RuleType.TYPE_SET;
    }

    @Override
    public int targetValue() {
        return Type.getTypes().size();
    }

    @Override
    public void applyProgress(
        BadgeProgress progress,
        TrashAnalysisEvent event,
        Clock clock
    ) {
        Type trashType = event.getTrashType();
        if (Type.getTypes().contains(trashType)) {
            progress.addTrashType(trashType.name());
        }
    }

    @Override
    public boolean achieved(BadgeProgress progress) {
        return progress.getUniqueTrashTypeCount() >= targetValue();
    }
}
