package com.trashheroesbe.feature.auth.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.auth.application.AuthService;
import com.trashheroesbe.feature.auth.dto.request.LoginPartnerRequest;
import com.trashheroesbe.feature.auth.dto.response.TokenVerifyResponse;
import com.trashheroesbe.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerApi {

    private final AuthService authService;

    @Override
    @GetMapping("/kakao/login")
    public ApiResponse<Void> login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/kakao");
        return ApiResponse.success(OK);
    }

    @Override
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        authService.invalidateCookie(response);
        return ApiResponse.success(OK);
    }

    @Override
    @PostMapping("/guest/login")
    public ApiResponse<Void> guestLogin(HttpServletResponse response) {
        authService.guestLogin(response);
        return ApiResponse.success(OK);
    }

    @Override
    @GetMapping("/verify")
    public ApiResponse<TokenVerifyResponse> verify(HttpServletRequest request) {
        TokenVerifyResponse response = authService.verifyToken(request);
        return ApiResponse.success(OK, response);
    }

    @Override
    @PostMapping("/partner/login")
    public ApiResponse<Void> partnerLogin(
        HttpServletResponse response,
        @RequestBody @Valid LoginPartnerRequest request
    ) throws IOException {
        authService.partnerLogin(response, request);
        return ApiResponse.success(OK);
    }
}