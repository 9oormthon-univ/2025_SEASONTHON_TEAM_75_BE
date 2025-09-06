package com.trashheroesbe.feature.trash.application;

import static com.trashheroesbe.feature.search.domain.LogSource.IMAGE;

import com.trashheroesbe.feature.disposal.infrastructure.DisposalRepository;
import com.trashheroesbe.feature.search.application.SearchLogService;
import com.trashheroesbe.feature.trash.domain.type.ItemType;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
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
    private final PlatformTransactionManager txManager;

    @Override
    public TrashResultResponse createTrash(CreateTrashRequest request, User user) {
        log.info("쓰레기 생성 시작: userId={}", user.getId());
        request.validate();

        var file = request.imageFile();
        String storedKey = FileUtils.generateStoredKey(
                Objects.requireNonNull(file.getOriginalFilename()), S3_TRASH_PREFIX);
        byte[] bytes;
        String contentType;
        try {
            bytes = file.getBytes();
            contentType = file.getContentType();
        } catch (Exception e) {
            log.error("파일 읽기 실패", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 업로드(S3)와 GPT 단일 분석 병렬 수행
        var uploadFuture = java.util.concurrent.CompletableFuture.supplyAsync(
                () -> fileStoragePort.uploadFile(storedKey, contentType, bytes)
        );
        var bundleFuture = java.util.concurrent.CompletableFuture.supplyAsync(
                () -> chatGPTClient.analyzeAll(bytes, contentType)
        );

        var bundle = bundleFuture.join();
        Type analyzedType = (bundle != null && bundle.type() != null) ? bundle.type() : Type.UNKNOWN;

        // name은 한 번에 받은 요약명
        String aiSummarizedName = (bundle != null) ? bundle.name() : null;

        // itemName은 항상 DB에서만 선택(요약명을 힌트로 사용)
        String itemName = pickItemNameFromDb(analyzedType, aiSummarizedName);

        // step1.item은 사용하지 않음(null)
        TrashAnalysisResponseDto step1 =
                TrashAnalysisResponseDto.of(TrashType.of(analyzedType), null, bundle != null ? bundle.description() : null);

        String imageUrl = uploadFuture.join();

        String finalName = decideTrashName(analyzedType, step1, itemName, aiSummarizedName);

        // DB 쓰기 (트랜잭션) - 기존 로직 유지
        Trash saved;
        try {
            TransactionTemplate writeTx = new TransactionTemplate(txManager);
            String finalItemName = itemName;
            saved = writeTx.execute(status -> {
                TrashType type = trashTypeRepository.findByType(analyzedType)
                        .orElseGet(() -> trashTypeRepository.save(TrashType.of(analyzedType)));

                Trash trash = Trash.create(user, imageUrl, finalName);
                trash.applyAnalysis(type);

                if (finalItemName != null && !finalItemName.isBlank()) {
                    String key = finalItemName.trim();
                    var item = trashItemRepository.findByTrashTypeAndName(type, key)
                            .orElseGet(() -> trashItemRepository.save(
                                    TrashItem.builder().trashType(type).name(key).build()
                            ));
                    trash.applyItem(item);
                }

                Trash persisted = trashRepository.save(trash);
                searchLogService.log(IMAGE, type, user);
                return persisted;
            });
        } catch (RuntimeException ex) {
            try { fileStoragePort.deleteFileByUrl(imageUrl); } catch (RuntimeException ignore) { }
            throw ex;
        }

        // 응답 구성 (읽기 전용 트랜잭션)
        TransactionTemplate readTx = new TransactionTemplate(txManager);
        readTx.setReadOnly(true);
        return readTx.execute(status -> {
            var descOpt = trashDescriptionRepository.findByTrashType(saved.getTrashType());
            var steps = descOpt.map(TrashDescription::steps).orElse(List.of());
            var caution = descOpt.map(TrashDescription::getCautionNote).orElse(null);

            var parts = suggestParts(
                    saved.getTrashType() != null ? saved.getTrashType().getType() : Type.UNKNOWN
            );

            var location = resolveUserDistrictSummary(user.getId());

            List<String> days = Collections.emptyList();
            if (location != null && saved.getTrashType() != null) {
                String did = location.id() != null ? location.id().trim() : null;
                days = resolveDisposalDays(did, saved.getTrashType().getType());
            }

            return TrashResultResponse.of(saved, steps, caution, days, parts, location);
        });
    }
    private String pickItemNameFromDb(Type type, String nameHint) {
        var items = trashItemRepository.findByTrashType_Type(type);
        if (items == null || items.isEmpty()) {
            log.warn("아이템 목록 없음: type={}", type);
            return null; // DB에 없으면 설정하지 않음
        }

        String hint = nameHint != null ? normalizeK(nameHint) : null;
        if (hint != null) {
            String best = null;
            int bestScore = -1;
            for (var it : items) {
                var nm = it.getName();
                if (nm == null || nm.isBlank()) continue;
                if (nm.contains("기타")) continue;
                int score = scoreByBigram(normalizeK(nm), hint);
                if (score > bestScore) { bestScore = score; best = nm; }
            }
            if (best != null) return best;
        }

        // 기본값: "기타" 우선, 없으면 null
        return items.stream()
                .map(com.trashheroesbe.feature.trash.domain.entity.TrashItem::getName)
                .filter(n -> n != null && !n.isBlank())
                .filter(n -> n.contains("기타"))
                .findFirst()
                .orElse(null);
    }

    private String normalizeK(String s) {
        return s.replaceAll("\\s+", "").toLowerCase();
    }

    private int scoreByBigram(String a, String b) {
        java.util.Set<String> sa = toBigrams(a);
        java.util.Set<String> sb = toBigrams(b);
        sa.retainAll(sb);
        return sa.size();
    }

    private java.util.Set<String> toBigrams(String s) {
        java.util.HashSet<String> set = new java.util.HashSet<>();
        if (s.length() < 2) { set.add(s); return set; }
        for (int i = 0; i < s.length() - 1; i++) set.add(s.substring(i, i + 2));
        return set;
    }

    private String decideTrashName(Type type, TrashAnalysisResponseDto step1, String itemName, String aiName) {
        if (aiName != null && !aiName.isBlank()) return aiName.trim();
        if (itemName != null && !itemName.isBlank()) return itemName.trim();
        if (step1 != null && step1.item() != null && !step1.item().isBlank()) return step1.item().trim();
        if (type == Type.FOOD_WASTE) return "음식물 쓰레기";
        if (type == Type.NON_RECYCLABLE) return "일반 쓰레기";
        return (type != null) ? type.getNameKo() : "쓰레기";
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
    @Transactional(readOnly = true)
    public List<TrashResultResponse> getTrashByUser(User user) {
        log.info("사용자별 쓰레기 조회 시작: userId={}", user.getId());

        List<Trash> trashes = trashRepository.findByUserOrderByCreatedAtDesc(user);

        List<TrashResultResponse> results = trashes.stream()
            .map(TrashResultResponse::from)
            .collect(Collectors.toList());

        log.info("사용자별 쓰레기 조회 완료: userId={}, count={}", user.getId(), results.size());
        return results;
    }

    @Transactional(readOnly = true)
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

        if(item.getItemType()== ItemType.CAUTION && item.getTrashType()!=null){
            trash.applyAnalysis(item.getRedirectTrashType());
        }

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