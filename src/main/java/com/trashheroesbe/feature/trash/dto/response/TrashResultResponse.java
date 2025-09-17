package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.entity.Trash;
import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TrashResultResponse(
        @Schema(description = "ID", example = "8")
        Long id,
        @Schema(description = "이미지 URL", example = "https://trashheroes.s3.ap-northeast-2.amazonaws.com/trash/20250904_003905_126ed9a4.jpg")
        String imageUrl,
        @Schema(description = "이름", example = "쓰레기")
        String name,
        @Schema(description = "세부 품목명", example = "PET(투명 페트병)")
        String itemName,
        @Schema(description = "타입 코드", example = "R04")
        String typeCode,
        @Schema(description = "타입명", example = "PET(투명 페트병)")
        String typeName,
        @Schema(
                description = "배출 가이드 단계",
                example = "[\"STEP 1: 내용물을 비우고 깨끗이 헹궈요.\", \"STEP 2: 비닐 라벨을 제거하여 비닐류로 배출해요.\", \"STEP 3: 페트병은 찌그러뜨린 뒤 뚜껑을 닫아 일반 플라스틱류와 구분하여 투명/반투명 봉투에 담아 배출해요.\"]"
        )
        List<String> guideSteps,
        @Schema(description = "주의사항", example = "주의: 유색·불투명 페트병이나 식용유병은 투명 페트병으로 분리하지 않아요.")
        String cautionNote,
        @ArraySchema(arraySchema = @Schema(description = "자치구 배출 요일(문자 배열)"))
        List<String> days,
        DistrictSummaryResponse location,
        @Schema(description = "부품 카드 목록")
        List<PartCardResponse> parts,
        @Schema(description = "새로 획득한 뱃지 목록")
        List<UserBadgeResponse> newBadges,
        @Schema(description = "생성 시각", example = "2025-09-04T00:39:17.853269")
        LocalDateTime createdAt
) {
    private static TrashResultResponseBuilder base(Trash t) {
        var tt = t.getTrashType() != null ? t.getTrashType().getType() : null;
        return TrashResultResponse.builder()
                .id(t.getId()).imageUrl(t.getImageUrl()).name(t.getName())
                .itemName(t.getTrashItem()!=null?t.getTrashItem().getName():null)
                .typeCode(tt!=null?tt.getTypeCode():null)
                .typeName(tt!=null?tt.getNameKo():null)
                .createdAt(t.getCreatedAt());
    }

    public static TrashResultResponse from(Trash t) {
        return base(t)
                .guideSteps(List.of())
                .cautionNote(null)
                .days(List.of())
                .parts(List.of())
                .location(null)
                .newBadges(List.of())
                .build();
    }

    public static TrashResultResponse of(Trash t, List<String> steps, String caution,
                                         List<String> days, List<PartCardResponse> parts,
                                         DistrictSummaryResponse location) {
        return base(t)
                .guideSteps(steps != null ? steps : List.of())
                .cautionNote(caution)
                .days(days != null ? days : List.of())
                .parts(parts != null ? parts : List.of())
                .location(location)
                .newBadges(List.of())
                .build();
    }

    public static TrashResultResponse of(Trash t, List<String> steps, String caution,
                                         List<String> days, List<PartCardResponse> parts,
                                         DistrictSummaryResponse location, List<UserBadgeResponse> newBadges) {
        return base(t)
                .guideSteps(steps != null ? steps : List.of())
                .cautionNote(caution)
                .days(days != null ? days : List.of())
                .parts(parts != null ? parts : List.of())
                .location(location)
                .newBadges(newBadges != null ? newBadges : List.of())
                .build();
    }
}