package com.trashheroesbe.feature.auth.controller;

import com.trashheroesbe.feature.auth.api.AuthControllerApi;
import com.trashheroesbe.feature.auth.service.KakaoAuthService;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.response.type.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/kakao")
public class KakaoAuthController implements AuthControllerApi {

    private final KakaoAuthService kakaoAuthService;

    @Override
    @GetMapping("/login")
    public ApiResponse<Void> login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/kakao");
        return ApiResponse.success(SuccessCode.OK);
    }

    @Override
    public ApiResponse<Void> logout(HttpServletResponse response) {
        kakaoAuthService.logout(response);
        return ApiResponse.success(SuccessCode.OK);
    }
}