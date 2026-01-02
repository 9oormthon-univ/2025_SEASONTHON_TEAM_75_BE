package com.trashheroesbe.feature.coupon.dto.request;

import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import com.trashheroesbe.feature.coupon.domain.type.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@Schema(description = "쿠폰 수정 요청(필요한 필드만 보내면 됩니다)")
public record CouponUpdateRequest(
    @Schema(description = "쿠폰 제목", example = "수정된 제목")
    String title,

    @Schema(description = "쿠폰 내용", example = "수정된 내용")
    String content,

    @Schema(description = "쿠폰 타입", example = "ONLINE")
    CouponType type,

    @Schema(description = "포인트 가격", example = "3000")
    @Min(0)
    Integer pointCost,

    @Schema(description = "할인 타입", example = "AMOUNT")
    DiscountType discountType,

    @Schema(description = "할인 값", example = "3000")
    @Positive
    Integer discountValue,

    @Schema(description = "총 재고", example = "100")
    @Positive
    Integer totalStock,

    @Schema(description = "활성 여부", example = "true")
    Boolean isActive
) {
}
