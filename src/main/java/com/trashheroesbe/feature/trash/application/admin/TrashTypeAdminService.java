package com.trashheroesbe.feature.trash.application.admin;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;
import static com.trashheroesbe.global.response.type.ErrorCode.S3_UPLOAD_FAIL;

import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.util.FileUtils;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class TrashTypeAdminService {

    private static final String S3_TRASH_TYPE_PREFIX = "trash-type/";

    private final FileStoragePort fileStoragePort;
    private final TrashTypeRepository trashTypeRepository;

    public void initializeTrashType() {
        for (var type : Type.values()) {
            trashTypeRepository.findByType(type)
                .orElseGet(() -> trashTypeRepository.save(TrashType.of(type)));
        }
    }

    public void uploadTrashTypeImage(Long trashTypeId, MultipartFile image) {
        TrashType trashType = trashTypeRepository.findById(trashTypeId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (image != null && !image.isEmpty()) {
            String imageUrl;
            String storedKey = FileUtils.generateStoredKey(
                Objects.requireNonNull(image.getOriginalFilename()), S3_TRASH_TYPE_PREFIX);
            try {
                imageUrl = fileStoragePort.uploadFile(
                    storedKey,
                    image.getContentType(),
                    image.getBytes()
                );
            } catch (Exception e) {
                throw new BusinessException(S3_UPLOAD_FAIL);
            }
            trashType.updateImageUrl(imageUrl);
        }
    }
}
