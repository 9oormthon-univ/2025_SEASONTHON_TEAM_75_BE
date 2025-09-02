package com.trashheroesbe.feature.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserRequest(
    @Schema(description = "닉네임", example = "수정닉네임")
    String nickname
) {

}
