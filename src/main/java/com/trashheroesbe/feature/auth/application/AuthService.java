package com.trashheroesbe.feature.auth.application;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.GUEST_ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.trashheroesbe.feature.auth.dto.response.TokenVerifyResponse;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.type.Role;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.auth.jwt.entity.JwtToken;
import com.trashheroesbe.global.auth.jwt.service.CookieProvider;
import com.trashheroesbe.global.auth.jwt.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public void invalidateCookie(HttpServletResponse response) {
        Cookie expiredAccessCookie = cookieProvider.createExpiredCookie(ACCESS_TOKEN);
        Cookie expiredRefreshCookie = cookieProvider.createExpiredCookie(REFRESH_TOKEN);

        response.addCookie(expiredAccessCookie);
        response.addCookie(expiredRefreshCookie);

        SecurityContextHolder.clearContext();
    }

    public void guestLogin(HttpServletResponse response) {
        User guestUser = User.createGuestUser();
        userRepository.save(guestUser);
        JwtToken jwtToken = jwtTokenProvider.generateGuestToken(guestUser);

        Cookie guestAccessCookie = cookieProvider.createTokenCookie(
            GUEST_ACCESS_TOKEN, jwtToken.getAccessToken());
        Cookie roleCheckCookie = cookieProvider.createRoleCheckCookie(
            GUEST_ACCESS_TOKEN, guestUser);

        response.addCookie(guestAccessCookie);
        response.addCookie(roleCheckCookie);
    }

    public TokenVerifyResponse verifyToken(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.extractTokenFromCookie(
            request,
            ACCESS_TOKEN.getName()
        );

        if (accessToken == null) {
            return TokenVerifyResponse.of(null, false);
        }

        boolean isValid = jwtTokenProvider.validateToken(accessToken);
        if (!isValid) {
            return TokenVerifyResponse.of(null, false);
        }

        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String authorities = claims.get("auth", String.class);

        Role role = jwtTokenProvider.extractRoleFromAuthorities(authorities);

        return TokenVerifyResponse.of(role, true);
    }
}
