package com.trashheroesbe.feature.point.dto.response;

import java.time.LocalDateTime;

public record PointEarnedResult(
    Integer earnedPoints,
    LocalDateTime createAt
) {

}
