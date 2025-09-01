package com.trashheroesbe.feature.district.dto.response;

import com.trashheroesbe.feature.district.domain.entity.District;
import lombok.Builder;

@Builder
public record DistrictListResponse(
    String districtId,
    String sido,
    String sigugn,
    String eupmyeondong
) {

    public static DistrictListResponse from(District district) {
        return DistrictListResponse.builder()
            .districtId(district.getId())
            .sido(district.getSido())
            .sigugn(district.getSigungu())
            .eupmyeondong(district.getEupmyeondong())
            .build();
    }
}
