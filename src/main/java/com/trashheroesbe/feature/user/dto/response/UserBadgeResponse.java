package com.trashheroesbe.feature.user.dto.response;

import com.trashheroesbe.feature.user.domain.entity.UserBadge;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@Schema(description = "사용자 뱃지 정보")
public record UserBadgeResponse(
    @Schema(description = "뱃지 ID", example = "1")
    Long badgeId,

    @Schema(description = "뱃지 이름", example = "첫 분리수거")
    String badgeName,

    @Schema(description = "뱃지 설명", example = "첫 번째 쓰레기 분리수거를 완료했습니다! 환경 보호의 첫걸음을 내디뎠어요.")
    String badgeDescription,

    @Schema(description = "뱃지 획득 조건 설명", example = "쓰레기 분석 1회 완료")
    String ruleTypeDescription,

    @Schema(description = "뱃지 획득 일시", example = "2024-03-15T14:30:00")
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
