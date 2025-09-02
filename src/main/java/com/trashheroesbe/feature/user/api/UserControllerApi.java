package com.trashheroesbe.feature.user.api;

import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "User", description = "유저 관련 API")
public interface UserControllerApi {

    @Operation(summary = "유저 수정하기(patch)", description = "유저의 정보를 수정합니다.(nickname, profileImg")
    ApiResponse<Long> updateUser(
        @Parameter(description = "수정할 사용자 정보 JSON")
        UpdateUserRequest request,

        @Parameter(description = "업로드할 이미지 파일")
        MultipartFile image,

        CustomerDetails customerDetails
    );

    @Operation(summary = "유저 자치구 추가하기", description = "유저의 자치구를 추가합니다.")
    ApiResponse<Void> createUserDistrict(CustomerDetails customerDetails, String districtId);

    @Operation(summary = "유저 자치구 삭제하기", description = "유저의 자치구를 삭제합니다.")
    ApiResponse<Void> deleteUserDistrict(Long userDistrictId);

    @Operation(summary = "내 자치구 조회하기", description = "나의 등록된 자치구를 조회합니다.")
    ApiResponse<List<DistrictListResponse>> getMyDistricts(CustomerDetails customerDetails);
}
