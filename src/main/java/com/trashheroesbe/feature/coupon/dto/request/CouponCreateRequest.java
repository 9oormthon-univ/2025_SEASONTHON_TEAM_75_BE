package com.trashheroesbe.feature.coupon.dto.request;


import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import com.trashheroesbe.feature.coupon.domain.type.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "쿠폰 생성 요청")
public record CouponCreateRequest(
        @NotBlank
        @Schema(description = "쿠폰 제목", example = "어스어스 3000원 할인 쿠폰")
        String title,

        @NotBlank
        @Schema(description = "쿠폰 내용", example = "어스어스 3000원을 할인 구매할 수 있는 쿠폰")
        String content,

        @NotNull
        @Schema(description = "쿠폰 타입", example = "ONLINE")
        CouponType type,

        @NotNull
        @PositiveOrZero
        @Schema(description = "포인트 가격", example = "3000")
        Integer pointCost,

        @NotNull
        @Schema(description = "할인 타입", example = "AMOUNT")
        DiscountType discountType,

        @NotNull
        @Positive
        @Schema(description = "할인 값", example = "3000")
        Integer discountValue
) {
    @AssertTrue(message = "퍼센트 할인은 100 이하로 입력해야 합니다.")
    public boolean isPercentDiscountValid() {
        return discountType != DiscountType.PERCENT || discountValue == null || discountValue <= 100;
    }
}
