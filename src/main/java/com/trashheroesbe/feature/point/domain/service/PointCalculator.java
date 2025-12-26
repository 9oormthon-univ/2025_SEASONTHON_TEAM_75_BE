package com.trashheroesbe.feature.point.domain.service;

import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointCalculator {

    private static final int MIN_POINT = 10;
    private static final int MAX_POINT = 50;

    public Integer calculateRandomPoints() {
        int points = ThreadLocalRandom.current().nextInt(MIN_POINT, MAX_POINT + 1);

        log.debug("랜덤 포인트 생성: {}", points);
        return points;
    }
}
