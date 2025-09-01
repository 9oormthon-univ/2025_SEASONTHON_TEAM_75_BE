package com.trashheroesbe.feature.trash.application;

import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.dto.response.TrashResult;
import com.trashheroesbe.feature.trash.domain.Trash;
import com.trashheroesbe.feature.trash.infrastructure.TrashRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrashService implements TrashCreateUseCase {

    private final TrashRepository trashRepository;
    private final FileStoragePort fileStoragePort;

    @Override
    @Transactional
    public TrashResult createTrash(CreateTrashRequest request, User user) {
        log.info("쓰레기 생성 시작: userId={}", user.getId());

        request.validate();

        try {
            String storedFileName = generateStoredFileName(
                    Objects.requireNonNull(request.imageFile().getOriginalFilename()));
            String imageUrl = fileStoragePort.uploadFile(
                    storedFileName,
                    request.imageFile().getContentType(),
                    request.imageFile().getBytes()
            );

            // Trash 엔티티 생성 및 저장
            Trash trash = Trash.create(user, imageUrl, "쓰레기 임시이름");
            Trash savedTrash = trashRepository.save(trash);

            log.info("쓰레기 생성 완료: ID={}, imageUrl={}", savedTrash.getId(), imageUrl);

            return TrashResult.from(savedTrash);

        } catch (Exception e) {
            log.error("쓰레기 생성 실패: userId={}", user.getId(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 사용자의 모든 쓰레기를 최신순으로 조회
     */
    public List<TrashResult> getTrashByUser(User user) {
        log.info("사용자별 쓰레기 조회 시작: userId={}", user.getId());
        
        List<Trash> trashes = trashRepository.findByUserOrderByCreatedAtDesc(user);
        
        List<TrashResult> results = trashes.stream()
                .map(TrashResult::from)
                .collect(Collectors.toList());
        
        log.info("사용자별 쓰레기 조회 완료: userId={}, count={}", user.getId(), results.size());
        return results;
    }

    /**
     * 저장될 파일명을 생성합니다.
     * 형식: trash/YYYYMMDD_HHmmss_UUID_확장자
     */
    private String generateStoredFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        return String.format("trash/%s_%s%s", timestamp, uuid, extension);
    }
}