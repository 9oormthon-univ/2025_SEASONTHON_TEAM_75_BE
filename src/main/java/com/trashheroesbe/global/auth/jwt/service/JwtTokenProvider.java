package com.trashheroesbe.global.auth.jwt.service;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.trashheroesbe.global.auth.jwt.entity.JwtToken;
import com.trashheroesbe.global.auth.jwt.entity.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

//    private static final long ACCESS_EXPIRE_MS = 1000L * 60 * 30; // 30분
//    private static final long REFRESH_EXPIRE_MS = 1000L * 60 * 60 * 24 * 7; // 7일

    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(","));

        TokenDto tokenDto = createAllToken(authentication.getName(), authorities);

        return JwtToken.builder()
            .accessToken(tokenDto.accessToken())
            .refreshToken(tokenDto.refreshToken())
            .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return true;

        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.", e);
            return false;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 토큰입니다.", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않은 토큰입니다.", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims에 정보가 없습니다.", e);
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private TokenDto createAllToken(String subject, String role) {
        return TokenDto.builder()
            .accessToken(createToken(subject, role, ACCESS_TOKEN.getValidTime()))
            .refreshToken(createToken(subject, role, REFRESH_TOKEN.getValidTime()))
            .build();
    }

    private String createToken(String subject, String authorities, long expire) {
        Instant now = Instant.now();
        Instant expireAt = now.plusMillis(expire);

        return Jwts.builder()
            .subject(subject)
            .claim("auth", authorities)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expireAt))
            .signWith(key)
            .compact();
    }
}
