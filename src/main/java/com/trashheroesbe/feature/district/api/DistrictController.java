package com.trashheroesbe.feature.district.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.district.application.DistrictService;
import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/districts")
public class DistrictController implements DistrictControllerApi {

    private final DistrictService districtService;

    @Override
    @GetMapping
    public ApiResponse<List<DistrictListResponse>> getDistrictList(
        @RequestParam String sido,
        @RequestParam(required = false) String sigungu
    ) {
        List<DistrictListResponse> response = districtService.getDistrictList(sido, sigungu);
        return ApiResponse.success(OK, response);
    }
}
