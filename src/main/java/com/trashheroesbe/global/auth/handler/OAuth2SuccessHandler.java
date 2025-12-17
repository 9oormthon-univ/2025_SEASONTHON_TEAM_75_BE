package com.trashheroesbe.global.auth.handler;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserDistrictRepository;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.auth.jwt.entity.JwtToken;
import com.trashheroesbe.global.auth.jwt.service.CookieProvider;
import com.trashheroesbe.global.auth.jwt.service.JwtTokenProvider;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;
    private final UserRepository userRepository;
    private final UserDistrictRepository userDistrictRepository;

    @Value("${frontend.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        String kakaoId = null;
        if (authentication instanceof OAuth2AuthenticationToken oauth) {
            Map<String, Object> attrs = oauth.getPrincipal().getAttributes();
            kakaoId = String.valueOf(attrs.get("id"));
        }

        User user = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (user != null) {
            CustomerDetails userDetails = new CustomerDetails(user);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );

            JwtToken jwtToken = jwtTokenProvider.generateToken(newAuth);

            Cookie accessTokenCookie = cookieProvider.createTokenCookie(
                ACCESS_TOKEN, jwtToken.getAccessToken());
            Cookie refreshTokenCookie = cookieProvider.createTokenCookie(
                REFRESH_TOKEN, jwtToken.getRefreshToken());
            Cookie roleCheckCookie = cookieProvider.createRoleCheckCookie(ACCESS_TOKEN, user);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            response.addCookie(roleCheckCookie);
        }

        boolean hasProfile = false;
        if (user != null) {
            // 기본 자치구가 하나라도 있는지로 프로필 설정 여부 판단
            hasProfile = userDistrictRepository.findByUserId(user.getId())
                .stream()
                .anyMatch(ud -> Boolean.TRUE.equals(ud.getIsDefault()));
        }

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
            .path(hasProfile ? "/home" : "/profile")
            .queryParam("success", true)
            .build()
            .toUriString();

        response.sendRedirect(redirectUrl);

    }
}