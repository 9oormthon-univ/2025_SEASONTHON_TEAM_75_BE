package com.trashheroesbe.feature.auth.dto.response;

import com.trashheroesbe.feature.user.domain.type.Role;
import lombok.Builder;

@Builder
public record TokenVerifyResponse(
    Role role,
    boolean isTokenVerified
) {

    public static TokenVerifyResponse of(Role role, Boolean isTokenVerified) {
        return TokenVerifyResponse.builder()
            .role(role)
            .isTokenVerified(isTokenVerified)
            .build();
    }
}
