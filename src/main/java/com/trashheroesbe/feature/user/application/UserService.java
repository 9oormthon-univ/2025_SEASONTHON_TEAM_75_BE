package com.trashheroesbe.feature.user.application;

import static com.trashheroesbe.global.response.type.ErrorCode.DUPLICATE_USER_DISTRICT;
import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;
import static com.trashheroesbe.global.response.type.ErrorCode.MAX_USER_DISTRICTS_EXCEEDED;
import static com.trashheroesbe.global.response.type.ErrorCode.S3_UPLOAD_FAIL;

import com.trashheroesbe.feature.district.domain.entity.District;
import com.trashheroesbe.feature.district.domain.service.DistrictFinder;
import com.trashheroesbe.feature.district.dto.response.DistrictListResponse;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.entity.UserDistrict;
import com.trashheroesbe.feature.user.domain.service.UserDistrictFinder;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.feature.user.infrastructure.UserDistrictRepository;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.util.FileUtils;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private static final String S3_USER_PREFIX = "user/";
    private static final int MAX_USER_DISTRICTS = 2;

    private final FileStoragePort fileStoragePort;

    private final DistrictFinder districtFinder;
    private final UserFinder userFinder;
    private final UserDistrictFinder userDistrictFinder;
    private final UserDistrictRepository userDistrictRepository;
    private final UserRepository userRepository;


    @Transactional
    public void updateUser(UpdateUserRequest request, MultipartFile image, Long userId) {
        User user = userFinder.findById(userId);

        if (request.nickname() != null) {
            user.updateNickname(request.nickname());
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl;
            String storedKey = FileUtils.generateStoredKey(
                Objects.requireNonNull(image.getOriginalFilename()), S3_USER_PREFIX);

            try {
                imageUrl = fileStoragePort.uploadFile(
                    storedKey,
                    image.getContentType(),
                    image.getBytes()
                );
            } catch (Exception e) {
                throw new BusinessException(S3_UPLOAD_FAIL);
            }
            user.updateProfileImageUrl(imageUrl);
        }
    }

    @Transactional
    public void createUserDistrict(Long userId, String districtId) {
        List<UserDistrict> userDistricts = userDistrictFinder.findByUserId(userId);

        if (userDistricts.size() >= MAX_USER_DISTRICTS) {
            throw new BusinessException(MAX_USER_DISTRICTS_EXCEEDED);
        }

        boolean alreadyExists = userDistricts.stream()
            .anyMatch(ud -> ud.getDistrict().getId().equals(districtId));

        if (alreadyExists) {
            throw new BusinessException(DUPLICATE_USER_DISTRICT);
        }

        User user = userFinder.findById(userId);
        District district = districtFinder.findById(districtId);

        UserDistrict userDistrict = UserDistrict.createUserDistrict(user, district);
        userDistrictRepository.save(userDistrict);
    }

    @Transactional
    public void deleteUserDistrict(Long userDistrictId) {
        if (!userDistrictFinder.existsByUserDistrictId(userDistrictId)) {
            throw new BusinessException(ENTITY_NOT_FOUND);
        }
        userDistrictRepository.deleteById(userDistrictId);
    }

    public List<DistrictListResponse> getUserDistrictsByUserId(Long userId) {
        List<UserDistrict> userDistricts = userDistrictFinder.findByUserIdFetchJoin(userId);

        return userDistricts.stream()
            .map(UserDistrict::getDistrict)
            .filter(Objects::nonNull)
            .map(DistrictListResponse::from)
            .toList();  // Jav
    }
}
