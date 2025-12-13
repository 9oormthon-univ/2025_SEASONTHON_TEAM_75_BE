package com.trashheroesbe.feature.coupon.dto.request;

import com.trashheroesbe.feature.coupon.domain.Coupon.DiscountType;
import com.trashheroesbe.feature.coupon.domain.Coupon.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰 생성 요청")
public record CouponCreateRequest(
        @Schema(description = "파트너 ID", example = "1")
        Long partnerId,
        @Schema(description = "쿠폰 제목", example = "어스어스 3000원 할인 쿠폰")
        String title,
        @Schema(description = "쿠폰 내용", example = "어스어스 3000원을 할인 구매할 수 있는 쿠폰")
        String content,
        @Schema(description = "쿠폰 타입", example = "ONLINE")
        CouponType type,
        @Schema(description = "포인트 가격", example = "3000")
        Integer pointCost,
        @Schema(description = "할인 타입", example = "AMOUNT")
        DiscountType discountType,
        @Schema(description = "할인 값", example = "3000")
        Integer discountValue
) {
    public void validate() {
        if (partnerId == null) throw new IllegalArgumentException("partnerId는 필수입니다.");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title은 필수입니다.");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content는 필수입니다.");
        if (type == null) throw new IllegalArgumentException("type은 ONLINE/OFFLINE 중 하나여야 합니다.");
        if (pointCost == null || pointCost < 0) throw new IllegalArgumentException("pointCost는 0 이상이어야 합니다.");
        if (discountType == null) throw new IllegalArgumentException("discountType은 AMOUNT/PERCENT 중 하나여야 합니다.");
        if (discountValue == null || discountValue <= 0) throw new IllegalArgumentException("discountValue는 양수여야 합니다.");
        if (discountType == DiscountType.PERCENT && discountValue > 100) {
            throw new IllegalArgumentException("퍼센트 할인은 100을 초과할 수 없습니다.");
        }
    }
}
