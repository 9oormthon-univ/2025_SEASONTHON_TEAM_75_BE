package com.trashheroesbe.feature.coupon.domain;

import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private Long partnerId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponType type;

    @Column(nullable = false)
    private Integer pointCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType discountType;

    @Column(nullable = false)
    private Integer discountValue;

    @Column(length = 200)
    private String qrToken;

    @Column(length = 500)
    private String qrImageUrl;

    public static Coupon create(CouponCreateRequest req) {
        return Coupon.builder()
                .partnerId(req.partnerId())
                .title(req.title())
                .content(req.content())
                .type(req.type())
                .pointCost(req.pointCost())
                .discountType(req.discountType())
                .discountValue(req.discountValue())
                .build();
    }

    public void attachQr(String qrToken, String qrImageUrl) {
        this.qrToken = qrToken;
        this.qrImageUrl = qrImageUrl;
    }

    public enum CouponType { ONLINE, OFFLINE }
    public enum DiscountType { AMOUNT, PERCENT }
}
