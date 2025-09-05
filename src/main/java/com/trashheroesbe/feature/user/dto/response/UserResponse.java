package com.trashheroesbe.feature.user.dto.response;

import com.trashheroesbe.feature.user.domain.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserResponse(
    Long userId,
    String nickName,
    String profileImageUrl,
    LocalDateTime createAt,
    LocalDateTime updatedAt
) {

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .userId(user.getId())
            .nickName(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .createAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
