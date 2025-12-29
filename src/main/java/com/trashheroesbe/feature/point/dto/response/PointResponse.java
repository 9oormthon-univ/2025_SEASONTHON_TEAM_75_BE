package com.trashheroesbe.feature.point.dto.response;

import java.time.LocalDateTime;

public record PointResponse(
    Integer earnPoints,
    LocalDateTime createdAt
) {

    public static PointResponse from(PointEarnedResult result) {
        return new PointResponse(result.earnedPoints(), result.createAt());
    }
}
