package com.trashheroesbe.feature.disposal.application;

import static com.trashheroesbe.global.response.type.ErrorCode.NOT_FOUND_DEFAULT_USER_DISTRICTS;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_FOUND_USER_DISTRICTS;

import com.trashheroesbe.feature.disposal.domain.Disposal;
import com.trashheroesbe.feature.disposal.dto.response.DistrictTrashDisposalResponse;
import com.trashheroesbe.feature.disposal.infrastructure.DisposalRepository;
import com.trashheroesbe.feature.user.domain.entity.UserDistrict;
import com.trashheroesbe.feature.user.domain.service.UserDistrictFinder;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.util.DateTimeUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DisposalService {

    private final UserDistrictFinder userDistrictFinder;
    private final DisposalRepository disposalRepository;

    public List<DistrictTrashDisposalResponse> getTodayTrashDisposal(Long userId) {
        List<UserDistrict> userDistricts = userDistrictFinder.findByUserId(userId);
        UserDistrict defaultUserDistrict = userDistricts.stream()
            .filter(UserDistrict::getIsDefault)
            .findFirst()
            .orElseThrow(() -> userDistricts.isEmpty()
                ? new BusinessException(NOT_FOUND_USER_DISTRICTS)
                : new BusinessException(NOT_FOUND_DEFAULT_USER_DISTRICTS)
            );

        String todayDay = DateTimeUtils.getTodayKorean();
        LocalDate todayDate = DateTimeUtils.getTodayDate();
        List<Disposal> disposals = disposalRepository.findAllByDistrictAndDay(
            defaultUserDistrict.getDistrict().getId(),
            todayDay
        );

        return DistrictTrashDisposalResponse.of(disposals, todayDay, todayDate);
    }
}
