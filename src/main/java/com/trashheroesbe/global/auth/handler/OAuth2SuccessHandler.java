package com.trashheroesbe.global.auth.handler;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.auth.jwt.entity.JwtToken;
import com.trashheroesbe.global.auth.jwt.service.CookieProvider;
import com.trashheroesbe.global.auth.jwt.service.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;

    @Value("${frontend.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        Cookie accessTokenCookie = cookieProvider.createTokenCookie(
            ACCESS_TOKEN, jwtToken.getAccessToken());
        Cookie refreshTokenCookie = cookieProvider.createTokenCookie(
            REFRESH_TOKEN, jwtToken.getRefreshToken());

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
            .queryParam("success", true)
            .build()
            .toUriString();

        response.sendRedirect(redirectUrl);
    }
}