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

    public void issue() {
        if (this.issuedCount >= this.totalStock) {
            throw new BusinessException(COUPON_OUT_OF_STOCK);
        }
        this.issuedCount++;
    }

    public void applyUpdate(
        String title,
        String content,
        CouponType type,
        Integer pointCost,
        DiscountType discountType,
        Integer discountValue,
        Integer totalStock,
        Boolean isActive
    ) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
        if (type != null) {
            this.type = type;
        }
        if (pointCost != null) {
            this.pointCost = pointCost;
        }
        if (discountType != null) {
            this.discountType = discountType;
        }
        if (discountValue != null) {
            this.discountValue = discountValue;
        }
        if (totalStock != null) {
            if (this.issuedCount > totalStock) {
                throw new IllegalArgumentException("재고는 이미 발급된 수량보다 작을 수 없습니다.");
            }
            this.totalStock = totalStock;
        }
        if (isActive != null) {
            this.isActive = isActive;
        }
    }
}
