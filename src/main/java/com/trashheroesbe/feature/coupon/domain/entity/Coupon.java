package com.trashheroesbe.feature.coupon.domain.entity;

import static com.trashheroesbe.global.response.type.ErrorCode.COUPON_OUT_OF_STOCK;

import com.trashheroesbe.feature.coupon.domain.type.CouponType;
import com.trashheroesbe.feature.coupon.domain.type.DiscountType;
import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.partner.domain.entity.Partner;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import com.trashheroesbe.global.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Entity
@Table(name = "coupons")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponType type;

    @Column(nullable = false)
    @Min(1)
    private Integer pointCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType discountType;

    @Column(nullable = false)
    @Min(1)
    private Integer discountValue;

    @Column(length = 200)
    private String qrToken;

    @Column(length = 500)
    private String qrImageUrl;

    @Column(nullable = false)
    private Integer totalStock;

    @Column(nullable = false)
    private Integer issuedCount;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @Version
    @Column(nullable = false)
    private Long version;

    public static Coupon create(CouponCreateRequest req, Partner partner) {
        return Coupon.builder()
            .partner(partner)
            .title(req.title())
            .content(req.content())
            .type(req.type())
            .pointCost(req.pointCost())
            .discountType(req.discountType())
            .discountValue(req.discountValue())
            .totalStock(req.totalStock())
            .issuedCount(0)
            .isActive(true)
            .version(0L)
            .build();
    }

    public void attachQr(String qrToken, String qrImageUrl) {
        if (qrToken == null || qrToken.isBlank()) {
            throw new IllegalArgumentException("QR 토큰은 필수입니다");
        }
        if (qrImageUrl == null || qrImageUrl.isBlank()) {
            throw new IllegalArgumentException("QR 이미지 URL은 필수입니다");
        }
        this.qrToken = qrToken;
        this.qrImageUrl = qrImageUrl;
    }

    public void issue() {
        if (this.issuedCount >= this.totalStock) {
            throw new BusinessException(COUPON_OUT_OF_STOCK);
        }
        this.issuedCount++;
    }
}
