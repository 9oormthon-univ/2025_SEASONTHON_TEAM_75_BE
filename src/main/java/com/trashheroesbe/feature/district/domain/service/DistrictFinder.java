package com.trashheroesbe.feature.district.domain.service;

import static com.trashheroesbe.global.response.type.ErrorCode.UNSUPPORTED_DISTRICT;

import com.trashheroesbe.feature.district.domain.entity.District;
import com.trashheroesbe.feature.district.infrastructure.DistrictRepository;
import com.trashheroesbe.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DistrictFinder {

    private final DistrictRepository districtRepository;

    public District findById(String distributeId) {
        return districtRepository.findById(distributeId)
            .orElseThrow(() -> new BusinessException(UNSUPPORTED_DISTRICT));
    }
}
