package com.trashheroesbe.global.auth.jwt.service;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.type.Role;
import com.trashheroesbe.global.auth.jwt.entity.TokenType;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public static final int MILLIS_PER_SECOND = 1000;

    @Value("${cookie.secure}")
    private boolean secureCookie;

    public Cookie createTokenCookie(TokenType tokenType, String token) {
        return createTokenCookie(tokenType.getName(), token, tokenType.getValidTime());
    }

    public Cookie createExpiredCookie(TokenType tokenType) {
        return createTokenCookie(tokenType.getName(), null, 0);
    }

    public Cookie createRoleCheckCookie(TokenType tokenType, User user) {
        if (user.getRole() == Role.GUEST) {
            return createRoleCookie("isMember", "false", tokenType.getValidTime());
        }
        return createRoleCookie("isMember", "true", tokenType.getValidTime());
    }

    private Cookie createTokenCookie(String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge((int) maxAge / MILLIS_PER_SECOND);
        cookie.setSecure(secureCookie);
        return cookie;
    }

    private Cookie createRoleCookie(String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge((int) maxAge / MILLIS_PER_SECOND);
        cookie.setSecure(false);
        return cookie;
    }
}
