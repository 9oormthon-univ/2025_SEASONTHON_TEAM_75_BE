package com.trashheroesbe.feature.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserRequest(
    @Schema(description = "닉네임", example = "수정닉네임")
    String nickname,

    @Schema(description = "선택한 자치구 Id(법정동코드)")
    String districtId
) {

}
