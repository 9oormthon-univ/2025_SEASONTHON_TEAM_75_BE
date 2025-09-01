package com.trashheroesbe.feature.user.application;

import static com.trashheroesbe.global.response.type.ErrorCode.S3_UPLOAD_FAIL;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.util.FileUtils;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final FileStoragePort fileStoragePort;

    private final UserFinder userFinder;
    private final UserRepository userRepository;

    @Transactional
    public void updateUser(UpdateUserRequest request, MultipartFile image, Long userId) {
        User user = userFinder.findById(userId);

        if (request.nickname() != null) {
            user.updateNickname(request.nickname());
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl;
            String fileName = FileUtils.generateStoredFileName(
                Objects.requireNonNull(image.getOriginalFilename()));

            try {
                imageUrl = fileStoragePort.uploadFile(
                    fileName, image.getContentType(), image.getBytes());
            } catch (Exception e) {
                throw new BusinessException(S3_UPLOAD_FAIL);
            }
            user.updateProfileImageUrl(imageUrl);
        }

    }
}
