package com.trashheroesbe.feature.point.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.point.application.PointService;
import com.trashheroesbe.feature.point.dto.response.UserPointResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointController implements PointControllerApi {

    private final PointService pointService;

    @Override
    @GetMapping
    public ApiResponse<UserPointResponse> createUserDistrict(CustomerDetails customerDetails) {
        UserPointResponse response = pointService.getMyPoint(customerDetails.getUser());
        return ApiResponse.success(OK, response);
    }
}
