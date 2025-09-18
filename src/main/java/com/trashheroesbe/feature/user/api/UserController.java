package com.trashheroesbe.feature.user.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.user.application.UserService;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import com.trashheroesbe.feature.user.dto.response.UserDistrictResponse;
import com.trashheroesbe.feature.user.dto.response.UserResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> updateUser(
        @RequestPart(value = "metadata") UpdateUserRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        Long userId = customerDetails.getUser().getId();
        userService.updateUser(request, image, userId);
        return ApiResponse.success(OK, userId);
    }

    @Override
    @PostMapping("/districts/{districtId}")
    public ApiResponse<List<UserDistrictResponse>> createUserDistrict(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable String districtId
    ) {
        List<UserDistrictResponse> response = userService.createUserDistrict(
            customerDetails.getUser().getId(), districtId);
        return ApiResponse.success(OK, response);
    }

    @Override
    @DeleteMapping("/districts/{userDistrictId}")
    public ApiResponse<List<UserDistrictResponse>> deleteUserDistrict(
        @PathVariable Long userDistrictId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<UserDistrictResponse> response = userService.deleteUserDistrict(
            userDistrictId, customerDetails.getUser().getId());
        return ApiResponse.success(OK, response);
    }

    @Override
    @GetMapping("/my/districts")
    public ApiResponse<List<UserDistrictResponse>> getMyDistricts(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<UserDistrictResponse> response = userService.getUserDistrictsByUserId(
            customerDetails.getUser().getId());
        return ApiResponse.success(OK, response);
    }

    @Override
    @PatchMapping("/districts/{userDistrictId}")
    public ApiResponse<List<UserDistrictResponse>> updateDefaultUserDistrict(
        @PathVariable Long userDistrictId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<UserDistrictResponse> response = userService.updateDefaultUserDistrict(
            userDistrictId, customerDetails.getUser().getId());
        return ApiResponse.success(OK, response);
    }

    @Override
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUserByToken(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        UserResponse response = UserResponse.from(customerDetails.getUser());
        return ApiResponse.success(OK, response);
    }

    @Override
    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUserByToken(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        HttpServletResponse httpResponse
    ) {
        userService.deleteUser(customerDetails.getUser(), httpResponse);
        return ApiResponse.success(OK);
    }

    @Override
    @GetMapping("/my/badges")
    public ApiResponse<List<UserBadgeResponse>> getMyBadges(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<UserBadgeResponse> response = userService.getUsrBadgesByUser(
            customerDetails.getUser());
        return ApiResponse.success(OK, response);
    }
}
