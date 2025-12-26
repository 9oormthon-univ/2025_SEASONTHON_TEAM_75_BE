package com.trashheroesbe.feature.point.application;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointManager {

    private final UserFinder userFinder;

    public void processPointEvent(
        Long userId,
        Integer randomPoints,
        String description,
        Long relatedEntityId
    ) {
        User user = userFinder.findById(userId);


    }
}
