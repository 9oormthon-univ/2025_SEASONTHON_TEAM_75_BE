package com.trashheroesbe.feature.auth.application;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.GUEST_ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.auth.jwt.entity.JwtToken;
import com.trashheroesbe.global.auth.jwt.service.CookieProvider;
import com.trashheroesbe.global.auth.jwt.service.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public void logout(HttpServletResponse response) {
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

        response.addCookie(guestAccessCookie);
    }
}
