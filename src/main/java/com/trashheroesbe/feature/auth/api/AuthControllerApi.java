package com.trashheroesbe.feature.auth.api;

import com.trashheroesbe.feature.auth.dto.request.LoginPartnerRequest;
import com.trashheroesbe.feature.auth.dto.response.TokenVerifyResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerApi {

    @Operation(summary = "kakao 로그인", description = "kakao 소셜 로그인을 진행합니다.")
    ApiResponse<Void> login(HttpServletResponse response) throws IOException;

    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    ApiResponse<Void> logout(HttpServletResponse response);

    @Operation(summary = "비회원 로그인", description = "비회원 로그인을 진행합니다. ")
    ApiResponse<Void> guestLogin(HttpServletResponse response);

    @Operation(summary = "토큰 정보 확인", description = "토큰 정보를 확인합니다. ")
    ApiResponse<TokenVerifyResponse> verify(HttpServletRequest request);

    @Operation(summary = "partner 로그인", description = "partner 로그인을 진행합니다.")
    ApiResponse<Void> partnerLogin(
        HttpServletResponse response,
        LoginPartnerRequest request
    ) throws IOException;

}