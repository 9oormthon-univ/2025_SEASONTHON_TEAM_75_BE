package com.trashheroesbe.global.auth.jwt.filter;

import static com.trashheroesbe.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.trashheroesbe.global.response.type.ErrorCode.INVALID_TOKEN;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TOKEN;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trashheroesbe.global.auth.jwt.entity.TokenType;
import com.trashheroesbe.global.auth.jwt.service.JwtTokenProvider;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.auth.security.CustomerDetailsService;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.util.CookieUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerDetailsService customerDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> ALLOWED = List.of(
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/v3/api-docs/**",
        "/api-docs/**",
        "/api/v1/auth/kakao/**",
        "/oauth2/**",
        "/login/**",
        "/favicon.ico"
    );

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        return ALLOWED.stream()
            .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String accessToken = CookieUtils.getCookieValue(request, ACCESS_TOKEN.getName());

            if (accessToken == null || accessToken.trim().isEmpty()) {
                throw new BusinessException(NOT_EXISTS_TOKEN);
            }

            if (!jwtTokenProvider.validateToken(accessToken)) {
                throw new BusinessException(INVALID_TOKEN);
            }

            Claims claims = jwtTokenProvider.parseClaims(accessToken);
            if (claims != null) {
                CustomerDetails customerUserDetails = customerDetailsService.loadUserByUsername(
                    claims.getSubject());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customerUserDetails, null, customerUserDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            handleException(response, e);
        }
    }

    private void handleException(
        HttpServletResponse response,
        BusinessException e
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(e.getErrorCode().getStatus().value());

        ApiResponse<?> apiResponse = ApiResponse.error(e.getErrorCode());
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();

        log.debug("에러 응답 전송 완료 - 상태코드: {}, 메시지: {}",
            e.getErrorCode().getStatus().value(), e.getErrorCode().getMessage());
    }
}
