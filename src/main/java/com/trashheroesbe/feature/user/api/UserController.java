package com.trashheroesbe.feature.user.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.feature.user.application.UserService;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.feature.user.dto.response.UserDistrictResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;

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
    public ApiResponse<Void> createUserDistrict(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @PathVariable String districtId
    ) {
        userService.createUserDistrict(customerDetails.getUser().getId(), districtId);
        return ApiResponse.success(OK);
    }

    @Override
    @DeleteMapping("/districts/{userDistrictId}")
    public ApiResponse<Void> deleteUserDistrict(
        @PathVariable Long userDistrictId
    ) {
        userService.deleteUserDistrict(userDistrictId);
        return ApiResponse.success(OK);
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
    public ApiResponse<Void> updateDefaultUserDistrict(
        @PathVariable Long userDistrictId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        userService.updateDefaultUserDistrict(userDistrictId, customerDetails.getUser().getId());
        return ApiResponse.success(OK);
    }
}
