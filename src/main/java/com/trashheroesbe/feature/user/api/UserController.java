package com.trashheroesbe.feature.user.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.user.application.UserService;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> updateUser(
        @RequestPart("metadata") UpdateUserRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @PathVariable Long userId
    ) {
        userService.updateUser(request, image, userId);
        return ApiResponse.success(OK, userId);
    }

    @Override
    @PostMapping("/{userId}/districts/{districtId}")
    public ApiResponse<Void> createUserDistrict(
        @PathVariable Long userId,
        @PathVariable String districtId
    ) {
        userService.createUserDistrict(userId, districtId);
        return ApiResponse.success(OK);
    }
}
