package com.trashheroesbe.feature.point.dto.response;

import com.trashheroesbe.feature.point.domain.entity.UserPoint;
import java.time.LocalDateTime;

public record UserPointResponse(
    Long userPointId,
    Integer totalPoint,
    LocalDateTime createAt,
    LocalDateTime updateAt
) {

    public static UserPointResponse from(UserPoint userPoint) {
        return new UserPointResponse(
            userPoint.getId(),
            userPoint.getTotalPoint(),
            userPoint.getCreatedAt(),
            userPoint.getUpdatedAt()
        );
    }
}
