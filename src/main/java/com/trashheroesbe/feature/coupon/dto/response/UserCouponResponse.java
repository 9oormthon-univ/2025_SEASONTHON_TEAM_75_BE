package com.trashheroesbe.feature.coupon.dto.response;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record UserCouponResponse(
    @Schema(description = "사용자 쿠폰 ID", example = "1")
    Long userCouponId,

    @Schema(description = "쿠폰 상태", example = "USED")
    CouponStatus couponStatus,

    @Schema(description = "구매 일시", type = "string", format = "date-time", example = "2026-03-14T10:00:00")
    LocalDateTime purchasedAt,

    @Schema(description = "수정 일시", type = "string", format = "date-time", example = "2026-03-14T12:00:00")
    LocalDateTime updateAt,

    @Schema(description = "쿠폰 ID", example = "10")
    Long couponId,

    @Schema(description = "쿠폰 제목", example = "아메리카노 1잔 무료")
    String couponTitle,

    @Schema(description = "쿠폰 내용", example = "매장 방문 시 아메리카노 1잔 제공")
    String couponContent,

    @Schema(description = "쿠폰 타입", example = "QR")
    CouponType couponType,

    @Schema(description = "QR 이미지 URL", example = "https://cdn.example.com/qr/1.png")
    String qrImageUrl,

    @Schema(description = "제휴처 이름", example = "스타카페")
    String partnerName,

    @Schema(description = "제휴처 이미지 URL", example = "https://cdn.example.com/partner/1.png")
    String partnerImageUrl,

    @Schema(description = "사용 일시", type = "string", format = "date-time", example = "2026-03-14T13:00:00", nullable = true)
    LocalDateTime usedAt,

    @Schema(description = "쿠폰 사용 가능 여부 정보")
    CouponUsabilityStatus usability
) {

    public static UserCouponResponse from(UserCoupon userCoupon) {
        return new UserCouponResponse(
            userCoupon.getId(),
            userCoupon.getStatus(),
            userCoupon.getCreatedAt(),
            userCoupon.getUpdatedAt(),
            userCoupon.getCoupon().getId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getContent(),
            userCoupon.getCoupon().getType(),
            userCoupon.getQrImageUrl(),
            userCoupon.getCoupon().getPartner().getPartnerName(),
            userCoupon.getCoupon().getPartner().getImageUrl(),
            userCoupon.getUsedAt(),
            null
        );
    }

    public static UserCouponResponse of(UserCoupon userCoupon, CouponUsabilityStatus usability) {
        return new UserCouponResponse(
            userCoupon.getId(),
            userCoupon.getStatus(),
            userCoupon.getCreatedAt(),
            userCoupon.getUpdatedAt(),
            userCoupon.getCoupon().getId(),
            userCoupon.getCoupon().getTitle(),
            userCoupon.getCoupon().getContent(),
            userCoupon.getCoupon().getType(),
            userCoupon.getQrImageUrl(),
            userCoupon.getCoupon().getPartner().getPartnerName(),
            userCoupon.getCoupon().getPartner().getImageUrl(),
            userCoupon.getUsedAt(),
            usability
        );
    }
}
