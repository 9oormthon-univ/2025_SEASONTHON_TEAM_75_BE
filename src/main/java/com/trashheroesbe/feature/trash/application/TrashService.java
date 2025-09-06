package com.trashheroesbe.feature.trash.application;

import static com.trashheroesbe.feature.search.domain.LogSource.IMAGE;

import com.trashheroesbe.feature.disposal.infrastructure.DisposalRepository;
import com.trashheroesbe.feature.search.application.SearchLogService;
import com.trashheroesbe.feature.trash.dto.response.*;
import com.trashheroesbe.feature.trash.domain.entity.TrashDescription;
import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.domain.entity.Trash;
import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.infrastructure.TrashDescriptionRepository;
import com.trashheroesbe.feature.trash.infrastructure.TrashItemRepository;
import com.trashheroesbe.feature.trash.infrastructure.TrashRepository;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserDistrictRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
import com.trashheroesbe.global.util.FileUtils;
import com.trashheroesbe.infrastructure.port.gpt.ChatAIClientPort;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrashService implements TrashCreateUseCase {

    private static final String S3_TRASH_PREFIX = "trash/";

    private final TrashRepository trashRepository;
    private final FileStoragePort fileStoragePort;
    private final ChatAIClientPort chatGPTClient;
    private final TrashTypeRepository trashTypeRepository;
    private final TrashItemRepository trashItemRepository;
    private final TrashDescriptionRepository trashDescriptionRepository;
    private final DisposalRepository disposalRepository;
    private final UserDistrictRepository userDistrictRepository;
    private final SearchLogService searchLogService;

    @Override
    @Transactional
    public TrashResultResponse createTrash(CreateTrashRequest request, User user) {
        log.info("쓰레기 생성 시작: userId={}", user.getId());
        request.validate();

        try {
            var file = request.imageFile();
            String storedKey = FileUtils.generateStoredKey(
                Objects.requireNonNull(file.getOriginalFilename()), S3_TRASH_PREFIX);
            byte[] bytes = file.getBytes();
            String contentType = file.getContentType();

            // 1) 타입 분석
            TrashAnalysisResponseDto step1 = chatGPTClient.analyzeType(bytes, contentType);

            // 2) 타입 결정
            Type analyzedType =
                (step1 != null && step1.type() != null && step1.type().getType() != null)
                    ? step1.type().getType() : Type.UNKNOWN;

            // 3) 타입 엔티티 조회/없으면 생성
            TrashType type = trashTypeRepository.findByType(analyzedType)
                .orElseGet(() -> trashTypeRepository.save(TrashType.of(analyzedType)));

            // 4) 재활용군이면 세부 품목 분석
            String itemName = null;
            if (isRecyclable(analyzedType)) {
                itemName = chatGPTClient.analyzeItem(bytes, contentType, analyzedType);
            }

            // 5) 업로드
            String imageUrl = fileStoragePort.uploadFile(storedKey, contentType, bytes);

            String displayName = displayNameFor(analyzedType);

            // 6) 저장(타입/요약 적용)
            Trash trash = Trash.create(user, imageUrl, displayName);
            trash.applyAnalysis(type, step1 != null ? step1.description() : null);

            // 7) 세부 품목 매핑(있으면)
            if (itemName != null && !itemName.isBlank()) {
                String key = itemName.trim();
                var item = trashItemRepository.findByTrashTypeAndName(type, key)
                    .orElseGet(() -> trashItemRepository.save(TrashItem.builder()
                        .trashType(type)
                        .name(key)
                        .build()));
                trash.applyItem(item);
            }

            Trash saved = trashRepository.save(trash);

            // 가이드/주의사항 조회
            var descOpt = trashDescriptionRepository.findByTrashType(type);
            var steps = descOpt.map(TrashDescription::steps).orElse(List.of());
            var caution = descOpt.map(TrashDescription::getCautionNote).orElse(null);

            // 부품 카드
            var parts = suggestParts(type.getType());

            // 사용자 기본 자치구/배출요일
            var location = resolveUserDistrictSummary(user.getId());

            log.info("쓰레기 생성 완료: id={}, userId={}, type={}, imageUrl={}",
                saved.getId(), user.getId(), type.getType(), imageUrl);

            var days = (location != null)
                ? resolveDisposalDays(location.id(), type.getType())
                : List.<String>of();

            searchLogService.log(IMAGE, type, user);
            return TrashResultResponse.of(saved, steps, caution, days, parts, location);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.error("쓰레기 생성 실패: userId={}", user.getId(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String displayNameFor(Type t) {
        return switch (t) {
            case FOOD_WASTE -> "음식물 쓰레기";
            case NON_RECYCLABLE -> "일반 쓰레기";
            default -> "쓰레기";
        };
    }

    private boolean isRecyclable(Type t) {
        return switch (t) {
            case PAPER, PAPER_PACK, PLASTIC, PET, VINYL_FILM, STYROFOAM, GLASS, METAL, TEXTILES,
                 E_WASTE,
                 HAZARDOUS_SMALL_WASTE -> true;
            default -> false;
        };
    }

    private List<PartCardResponse> suggestParts(Type baseType) {
        // PET일 때 예시와 동일하게 3개 추천
        if (baseType == Type.PET) {
            return List.of(
                PartCardResponse.of("페트병 뚜껑", Type.PET),
                PartCardResponse.of("투명 페트병 몸체", Type.PET),
                PartCardResponse.of("비닐 라벨", Type.VINYL_FILM)
            );
        }
        return List.of();
    }

    private DistrictSummaryResponse resolveUserDistrictSummary(Long userId) {
        var uds = userDistrictRepository.findByUserIdFetchJoin(userId);
        var udOpt = uds.stream()
            .filter(ud -> Boolean.TRUE.equals(ud.getIsDefault()))
            .findFirst()
            .or(() -> uds.stream().findFirst());
        return udOpt.map(ud -> {
            var d = ud.getDistrict();
            return new DistrictSummaryResponse(d.getId(), d.getSido(), d.getSigungu(),
                d.getEupmyeondong());
        }).orElse(null);
    }

    private List<String> resolveDisposalDays(String districtId, Type type) {
        if (districtId == null || type == null) {
            return List.of();
        }
        String did = districtId.trim();

        // 1) 먼저 정확히 해당 타입으로 조회
        var exact = disposalRepository.findByDistrict_IdAndTrashType_Type(did, type);
        if (exact.isPresent()) {
            return exact.get().getDays();
        }

        // 2) 없으면 정규화 타입으로 재조회
        Type fallback = (type == Type.PET) ? Type.PLASTIC : type;
        return disposalRepository.findByDistrict_IdAndTrashType_Type(did, fallback)
            .map(com.trashheroesbe.feature.disposal.domain.Disposal::getDays)
            .orElse(List.of());
    }

    /**
     * 특정 사용자의 모든 쓰레기를 최신순으로 조회
     */
    public List<TrashResultResponse> getTrashByUser(User user) {
        log.info("사용자별 쓰레기 조회 시작: userId={}", user.getId());

        List<Trash> trashes = trashRepository.findByUserOrderByCreatedAtDesc(user);

        List<TrashResultResponse> results = trashes.stream()
            .map(TrashResultResponse::from)
            .collect(Collectors.toList());

        log.info("사용자별 쓰레기 조회 완료: userId={}, count={}", user.getId(), results.size());
        return results;
    }

    public TrashResultResponse getTrash(Long trashId) {
        Trash trash = trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM));

        var descOpt = trashDescriptionRepository.findByTrashType(trash.getTrashType());
        var steps = descOpt.map(TrashDescription::steps)
            .orElse(List.of());
        var caution = descOpt.map(TrashDescription::getCautionNote)
            .orElse(null);

        var parts = suggestParts(
            trash.getTrashType() != null ? trash.getTrashType().getType() : Type.UNKNOWN
        );

        // 1) location(사용자 기본 자치구)
        var location = resolveUserDistrictSummary(trash.getUser().getId());

        // 2) days(enum 기반 조회, 정규화 + id trim)
        List<String> days = Collections.emptyList();
        if (location != null && trash.getTrashType() != null) {
            String did = location.id() != null ? location.id().trim() : null;
            days = resolveDisposalDays(did, trash.getTrashType().getType());
        }

        return TrashResultResponse.of(trash, steps, caution, days, parts, location);
    }

    @Transactional(readOnly = true)
    public List<TrashItemResponse> getTrashItemsByTrashId(Long trashId) {
        var trash = trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM));

        if (trash.getTrashType() == null) {
            return List.of();
        }

        return trashItemRepository.findByTrashTypeId(trash.getTrashType().getId())
            .stream()
            .map(TrashItemResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TrashResultResponse changeTrashItem(Long trashId, Long trashItemId, User user) {
        var trash = trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM));

        if (!trash.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM);
        }

        var item = trashItemRepository.findById(trashItemId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM));

        if (trash.getTrashType() == null || item.getTrashType() == null ||
            !item.getTrashType().getId().equals(trash.getTrashType().getId())) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM);
        }

        trash.applyItem(item);

        var descOpt = trashDescriptionRepository.findByTrashType(trash.getTrashType());
        var steps = descOpt.map(TrashDescription::steps).orElse(List.of());
        var caution = descOpt.map(TrashDescription::getCautionNote).orElse(null);

        var parts = suggestParts(trash.getTrashType().getType());

        // 사용자 기본 자치구 + 배출 요일(enum 기반)
        var district = resolveUserDistrictSummary(user.getId());
        List<String> days = Collections.emptyList();
        if (district != null && trash.getTrashType() != null) {
            days = resolveDisposalDays(district.id(), trash.getTrashType().getType());
        }

        return TrashResultResponse.of(trash, steps, caution, days, parts, district);
    }

    @Transactional
    public void deleteTrash(Long trashId, User user) {
        log.info("쓰레기 삭제 시작: userId={}, trashId={}", user.getId(), trashId);

        Trash trash = trashRepository.findById(trashId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM));

        if (!trash.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_TRASH_ITEM);
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