package com.trashheroesbe.feature.user.api;

import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "User", description = "유저 관련 API")
public interface UserControllerApi {

    @Operation(summary = "유저 수정하기(patch)", description = "유저의 정보를 수정합니다.(nickname, profileImg")
    ApiResponse<Long> updateUser(
        @Parameter(description = "수정할 사용자 정보 JSON")
        UpdateUserRequest request,

        @Parameter(description = "업로드할 이미지 파일")
        MultipartFile image,

        Long userId
    );
}
