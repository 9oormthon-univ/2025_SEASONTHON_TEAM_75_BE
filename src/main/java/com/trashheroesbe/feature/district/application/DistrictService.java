package com.trashheroesbe.feature.district.application;

import com.trashheroesbe.feature.district.domain.entity.District;
import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.feature.district.infrastructure.DistrictRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;

    public List<DistrictListResponse> getDistrictList(String sido, String sigungu) {

        List<District> districtList;
        if (sigungu == null) {
            districtList = districtRepository.findDistrictsBySido(sido);
        } else if (sido == null) {
            districtList = districtRepository.findDistrictBySigungu(sigungu);
        } else {
            districtList = districtRepository.findEupmyeondongBySidoAndSigungu(sido, sigungu);
        }

        return districtList.stream()
            .map(DistrictListResponse::from)
            .collect(Collectors.toList());
    }
}
