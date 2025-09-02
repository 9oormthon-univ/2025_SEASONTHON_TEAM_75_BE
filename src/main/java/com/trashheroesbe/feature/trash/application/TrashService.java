package com.trashheroesbe.feature.trash.application;

import com.trashheroesbe.feature.gpt.application.ChatGPTClient;
import com.trashheroesbe.feature.gpt.dto.response.TrashAnalysisResponseDto;
import com.trashheroesbe.feature.trash.domain.TrashItem;
import com.trashheroesbe.feature.trash.domain.TrashType;
import com.trashheroesbe.feature.trash.domain.Type;
import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.dto.response.TrashResult;
import com.trashheroesbe.feature.trash.domain.Trash;
import com.trashheroesbe.feature.trash.infrastructure.TrashItemRepository;
import com.trashheroesbe.feature.trash.infrastructure.TrashRepository;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import com.trashheroesbe.feature.user.domain.User;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;

import java.util.Objects;

import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
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
    private final ChatGPTClient chatGPTClient;
    private final TrashTypeRepository trashTypeRepository;
    private final TrashItemRepository trashItemRepository;

    @Override
    @Transactional
    public TrashResult createTrash(CreateTrashRequest request, User user) {
        log.info("쓰레기 생성 시작: userId={}", user.getId());
        request.validate();

        try {
            var file = request.imageFile();
            String storedFileName = generateStoredFileName(Objects.requireNonNull(file.getOriginalFilename()));
            byte[] bytes = file.getBytes();
            String contentType = file.getContentType();

            // 1) 타입 분석
            TrashAnalysisResponseDto step1 = chatGPTClient.analyzeType(bytes, contentType);

            // 2) 타입 결정
            Type analyzedType = (step1 != null && step1.type() != null && step1.type().getType() != null)
                    ? step1.type().getType() : Type.UNKNOWN;

            // 3) 타입 엔티티 조회/없으면 생성 → 여기서 'type' 정의
            TrashType type = trashTypeRepository.findByType(analyzedType)
                    .orElseGet(() -> trashTypeRepository.save(TrashType.of(analyzedType)));

            // 4) 재활용군이면 세부 품목 분석
            String itemName = null;
            if (isRecyclable(analyzedType)) {
                itemName = chatGPTClient.analyzeItem(bytes, contentType, analyzedType);
            }

            // 5) 업로드
            String imageUrl = fileStoragePort.uploadFile(storedFileName, null, contentType, bytes);

            // 6) 저장(타입/요약 적용)
            Trash trash = Trash.create(user, imageUrl, "쓰레기");
            trash.applyAnalysis(type, step1 != null ? step1.description() : null);

            // 7) 세부 품목 매핑(있으면)
            if (itemName != null && !itemName.isBlank()) {
                String key = itemName.trim();
                var item = trashItemRepository.findByTrashTypeAndName(type, key)
                        .orElseGet(() -> trashItemRepository.save(TrashItem.of(type, key)));
                trash.applyItem(item);
            }

            Trash saved = trashRepository.save(trash);
            log.info("쓰레기 생성 완료: id={}, userId={}, type={}, imageUrl={}",
                    saved.getId(), user.getId(), type.getType(), imageUrl);

            return TrashResult.from(saved);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.error("쓰레기 생성 실패: userId={}", user.getId(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isRecyclable(Type t) {
        switch (t) {
            case PAPER, PLASTIC, VINYL_FILM, STYROFOAM, GLASS, METAL, TEXTILES, E_WASTE, HAZARDOUS_SMALL_WASTE:
                return true;
            default:
                return false;
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

    @Transactional
    public void deleteTrash(Long trashId, User user) {
        log.info("쓰레기 삭제 시작: userId={}, trashId={}", user.getId(), trashId);

        Trash trash = trashRepository.findById(trashId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRASH_NOT_FOUND));

        if (!trash.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 1) S3 삭제
        try {
            fileStoragePort.deleteFileByUrl(trash.getImageUrl());
        } catch (RuntimeException ex) {
            throw new BusinessException(ErrorCode.S3_DELETE_FAIL);
        }

        // 2) DB 삭제
        trashRepository.delete(trash);

        log.info("쓰레기 삭제 완료: userId={}, trashId={}", user.getId(), trashId);
    }

}