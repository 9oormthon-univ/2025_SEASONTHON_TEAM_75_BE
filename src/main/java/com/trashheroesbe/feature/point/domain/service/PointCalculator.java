package com.trashheroesbe.feature.point.domain.service;

import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class PointCalculator {

    private static final int MIN_POINT = 10;
    private static final int MAX_POINT = 50;

    public Integer calculateRandomPoints() {
        int points = ThreadLocalRandom.current().nextInt(MIN_POINT, MAX_POINT + 1);

        // TODO : 추후에 쓰레기 타입별로, 뱃지 획득시 포인트 적립 등 포인트 생성 계산을 이쪽에서 담당
        return points;
    }
}
