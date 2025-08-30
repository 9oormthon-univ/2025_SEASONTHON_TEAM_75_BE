package com.trashheroesbe.feature.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Favicon", description = "Favicon 관련 API(리다이렉션 관련)")
@RestController
public class FaviconController {

    @Operation(summary = "favicon 호출 204 응답")
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build(); // 204
    }
}