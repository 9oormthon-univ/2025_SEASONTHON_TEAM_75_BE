package com.trashheroesbe.global.constants;

import java.util.List;

public final class SecurityConstants {

//    public static final String[] PERMIT_ALL = {
//        "/swagger-ui/**",
//        "/swagger-resources/**",
//        "/v3/api-docs/**",
//        "/api-docs/**",
//        "/api/v1/auth/**",
//        "/oauth2/**",
//        "/login/**",
//        "/favicon.ico"
//    };

    public static final List<String> ALLOWED = List.of(
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/v3/api-docs/**",
        "/api-docs/**",
        "/api/v1/auth/**",
        "/oauth2/**",
        "/login/**",
        "/favicon.ico"
    );
}
