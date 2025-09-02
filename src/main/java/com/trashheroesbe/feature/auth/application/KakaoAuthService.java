package com.trashheroesbe.feature.auth.application;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.trashheroesbe.global.auth.jwt.service.CookieProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final CookieProvider cookieProvider;

    public void logout(HttpServletResponse response) {
        Cookie expiredAccessToken = cookieProvider.createExpiredCookie(ACCESS_TOKEN);
        Cookie expiredRefreshToken = cookieProvider.createExpiredCookie(REFRESH_TOKEN);

        response.addCookie(expiredAccessToken);
        response.addCookie(expiredRefreshToken);

        SecurityContextHolder.clearContext();
    }

}
