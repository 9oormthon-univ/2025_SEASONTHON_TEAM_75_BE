package com.trashheroesbe.feature.user.dto.response;

import com.trashheroesbe.feature.user.domain.entity.UserBadge;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserBadgeResponse(
    Long badgeId,
    String badgeName,
    String badgeDescription,
    String ruleTypeDescription,
    LocalDateTime earnedAt
) {

    public static UserBadgeResponse from(UserBadge userBadge) {
        return UserBadgeResponse.builder()
            .badgeId(userBadge.getBadge().getId())
            .badgeName(userBadge.getBadge().getName())
            .badgeDescription(userBadge.getBadge().getDescription())
            .ruleTypeDescription(userBadge.getBadge().getRuleType().getDescription())
            .earnedAt(userBadge.getEarnedAt())
            .build();
    }
}
