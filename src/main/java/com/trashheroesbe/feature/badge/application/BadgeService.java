package com.trashheroesbe.feature.badge.application;

import com.trashheroesbe.feature.badge.domain.event.TrashAnalysisEvent;
import com.trashheroesbe.feature.trash.domain.type.Type;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final ApplicationEventPublisher publisher;


    public void onTrashAnalysisCompleted(Long userId, Type trashType) {
        TrashAnalysisEvent event = TrashAnalysisEvent.builder()
            .userId(userId)
            .trashType(trashType)
            .occurredAt(LocalDateTime.now())
            .build();

        publisher.publishEvent(event);
    }
}
