package com.trashheroesbe.feature.user.api;

import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "User", description = "유저 관련 API")
public interface UserControllerApi {

    @Operation(summary = "유저 수정하기(patch)", description = "유저의 정보를 수정합니다.(nickname, profileImg, district")
    ApiResponse<Long> updateUser(UpdateUserRequest request, Long userId);
}
