package com.trashheroesbe.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:8080", "http://127.0.0.1:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);

        registry.addMapping("/swagger-ui/**")
            .allowedOrigins("*")
            .allowedMethods("GET")
            .allowedHeaders("*");

        registry.addMapping("/v3/api-docs/**")
            .allowedOrigins("*")
            .allowedMethods("GET")
            .allowedHeaders("*");
    }
}
