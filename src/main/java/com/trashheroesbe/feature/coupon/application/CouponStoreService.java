package com.trashheroesbe.feature.coupon.application;

import static com.trashheroesbe.global.response.type.ErrorCode.COUPON_NOT_AVAILABLE;
import static com.trashheroesbe.global.response.type.ErrorCode.COUPON_NOT_FOUND;
import static com.trashheroesbe.global.response.type.ErrorCode.COUPON_OUT_OF_STOCK;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.dto.request.CouponPurchaseRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreListResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponStoreResponse;
import com.trashheroesbe.feature.coupon.dto.response.PurchaseUserCouponResponse;
import com.trashheroesbe.feature.coupon.infrastructure.CouponRepository;
import com.trashheroesbe.feature.coupon.infrastructure.UserCouponRepository;
import com.trashheroesbe.global.qrcode.QrCodeGenerator;
import com.trashheroesbe.global.util.UserCouponQrUtil;
import com.trashheroesbe.feature.point.application.PointService;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponStoreService {

    private final PointService pointService;

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final QrCodeGenerator qrCodeGenerator;
    private final FileStoragePort fileStoragePort;

    @Value("${qr.user-coupon-url}")
    private String userCouponQrBaseUrl;

    public List<CouponStoreListResponse> getCouponStoreList() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream()
            .map(CouponStoreListResponse::from)
            .collect(Collectors.toList());
    }

    public CouponStoreResponse getCouponStoreById(Long couponId) {
        Coupon coupon = couponRepository.findByIdFetchPartner(couponId)
            .orElseThrow(() -> new BusinessException(COUPON_NOT_FOUND));
        return CouponStoreResponse.from(coupon);
    }

    @Transactional
    @Retryable(
        retryFor = ObjectOptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public PurchaseUserCouponResponse purchaseCoupon(
        CouponPurchaseRequest request,
        User user
    ) {
        Coupon coupon = couponRepository.findByIdFetchPartner(request.couponId())
            .orElseThrow(() -> new BusinessException(COUPON_NOT_FOUND));

        if (!coupon.getIsActive()) {
            throw new BusinessException(COUPON_NOT_AVAILABLE);
        }

        if (coupon.getIssuedCount() >= coupon.getTotalStock()) {
            throw new BusinessException(COUPON_OUT_OF_STOCK);
        }

        Integer pointCost = coupon.getPointCost();
        pointService.usePoint(
            user.getId(),
            pointCost,
            PointReason.COUPON_PURCHASE,
            request.couponId()
        );

        coupon.issue();

        UserCoupon userCoupon = UserCoupon.create(user, coupon);
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        String qrToken = UUID.randomUUID().toString();
        String payload = UserCouponQrUtil.buildPayload(userCouponQrBaseUrl, savedUserCoupon.getId(), qrToken);
        byte[] qrBytes = qrCodeGenerator.generatePngBytes(payload, 300);
        String key = UserCouponQrUtil.buildKey(savedUserCoupon.getId());
        String qrUrl = fileStoragePort.uploadFile(key, "image/png", qrBytes);
        savedUserCoupon.attachQr(qrToken, qrUrl);

        log.info("쿠폰 구매 완료 - userId: {}, couponId: {}, pointsUsed: {}",
            user.getId(), request.couponId(), pointCost);

        return PurchaseUserCouponResponse.from(savedUserCoupon);
    }
}
