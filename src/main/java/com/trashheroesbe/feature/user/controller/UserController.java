package com.trashheroesbe.feature.user.controller;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.user.api.UserControllerApi;
import com.trashheroesbe.feature.user.application.UserService;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    @PatchMapping("/{userId}")
    public ApiResponse<Long> updateUser(
        @RequestBody UpdateUserRequest request,
        @PathVariable Long userId
    ) {
        userService.updateUser(request, userId);
        return ApiResponse.success(OK, userId);
    }
}
