package com.trashheroesbe.feature.coupon.domain.entity;


import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_coupons")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponStatus status;

    @Column
    private LocalDateTime usedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(length = 200)
    private String qrToken;

    @Column(length = 500)
    private String qrImageUrl;

    public static UserCoupon create(User user, Coupon coupon) {
        return UserCoupon.builder()
            .user(user)
            .coupon(coupon)
            .status(CouponStatus.AVAILABLE)
            .build();
    }

    public void attachQr(String qrToken, String qrImageUrl) {
        if (qrToken == null || qrToken.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED);
        }
        if (qrImageUrl == null || qrImageUrl.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED);
        }
        this.qrToken = qrToken;
        this.qrImageUrl = qrImageUrl;
    }
}
