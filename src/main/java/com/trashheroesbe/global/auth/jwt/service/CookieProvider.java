package com.trashheroesbe.global.auth.jwt.service;

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
        return createCookie(tokenType.getName(), token, tokenType.getValidTime());
    }

    public Cookie createExpiredCookie(TokenType tokenType) {
        return createCookie(tokenType.getName(), null, 0);
    }

    private Cookie createCookie(String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge((int) maxAge / MILLIS_PER_SECOND);
        cookie.setSecure(secureCookie);
        return cookie;
    }
}
