package com.trashheroesbe.feature.user.dto.response;

import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.feature.user.domain.entity.UserDistrict;
import lombok.Builder;

@Builder
public record UserDistrictResponse(
    DistrictListResponse response,
    Long userDistrictId,
    Boolean isDefault
) {

    public static UserDistrictResponse from(UserDistrict userDistrict) {
        return UserDistrictResponse.builder()
            .response(DistrictListResponse.from(userDistrict.getDistrict()))
            .userDistrictId(userDistrict.getId())
            .isDefault(userDistrict.getIsDefault())
            .build();
    }
}
