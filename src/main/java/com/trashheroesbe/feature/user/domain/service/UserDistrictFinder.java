package com.trashheroesbe.feature.user.domain.service;

import com.trashheroesbe.feature.user.domain.entity.UserDistrict;
import com.trashheroesbe.feature.user.infrastructure.UserDistrictRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDistrictFinder {

    private final UserDistrictRepository userDistrictRepository;

    public Boolean existsByUserIdAndDistrictId(Long userId, String districtId) {
        return userDistrictRepository.existsByUserIdAndDistrictId(userId, districtId);
    }

    public Boolean existsByUserDistrictId(Long userDistrictId) {
        return userDistrictRepository.existsById(userDistrictId);
    }

    public List<UserDistrict> findByUserId(Long userId) {
        return userDistrictRepository.findByUserId(userId);
    }
}
