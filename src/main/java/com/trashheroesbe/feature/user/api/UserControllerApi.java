package com.trashheroesbe.feature.user.api;

import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import com.trashheroesbe.feature.user.dto.response.UserDistrictResponse;
import com.trashheroesbe.feature.user.dto.response.UserResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "User", description = "유저 관련 API")
public interface UserControllerApi {

    @Operation(summary = "유저 수정하기(patch)", description = "유저의 정보를 수정합니다.(nickname, profileImg)")
    ApiResponse<Long> updateUser(

        @RequestPart(value = "metadata")
        UpdateUserRequest request,

        @Parameter(description = "업로드할 이미지 파일")
        @RequestPart(value = "image", required = false)
        MultipartFile image,

        @Parameter(hidden = true)  //
        CustomerDetails customerDetails
    );

    @Operation(summary = "유저 자치구 추가하기", description = "유저의 자치구를 추가합니다.")
    ApiResponse<List<UserDistrictResponse>> createUserDistrict(CustomerDetails customerDetails,
        String districtId);

    @Operation(summary = "유저 자치구 삭제하기", description = "유저의 자치구를 삭제합니다.")
    ApiResponse<List<UserDistrictResponse>> deleteUserDistrict(
        Long userDistrictId,
        CustomerDetails customerDetails
    );

    @Operation(summary = "내 자치구 조회하기", description = "나의 등록된 자치구를 조회합니다.")
    ApiResponse<List<UserDistrictResponse>> getMyDistricts(CustomerDetails customerDetails);

    @Operation(summary = "유저 대표(default) 자치구 수정하기", description = "유저 자치구의 default 값을 수정합니다.")
    ApiResponse<List<UserDistrictResponse>> updateDefaultUserDistrict(
        Long userDistrictId,
        CustomerDetails customerDetails
    );

    @Operation(summary = "유저 상세조회(me)", description = "토큰 정보로 유저를 조회합니다")
    ApiResponse<UserResponse> getUserByToken(CustomerDetails customerDetails);

    @Operation(summary = "유저 탈퇴", description = "토큰 정보로 유저를 삭제합니다.")
    ApiResponse<Void> deleteUserByToken(CustomerDetails customerDetails);

    @Operation(summary = "내 뱃지 리스트 조회", description = "내가 획득한 뱃지 리스트를 조회합니다.")
    ApiResponse<List<UserBadgeResponse>> getMyBadges(CustomerDetails customerDetails);
}
