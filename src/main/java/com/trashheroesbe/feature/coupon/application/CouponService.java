package com.trashheroesbe.feature.coupon.application;

import com.trashheroesbe.feature.coupon.domain.Coupon;
import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponQrResponse;
import com.trashheroesbe.feature.coupon.infrastructure.CouponRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.qrcode.QrCodeGenerator;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import com.trashheroesbe.global.response.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final QrCodeGenerator qrCodeGenerator;
    private final FileStoragePort fileStoragePort;

    @Value("${qr.coupon-url}")
    private String couponQrBaseUrl;

    public CouponCreateResponse createCoupon(CouponCreateRequest request) {
        request.validate();
        Coupon saved = couponRepository.save(Coupon.create(request));
        issueQr(saved);
        return CouponCreateResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public CouponQrResponse findByQr(Long couponId, String qrToken) {
        if (couponId == null || qrToken == null || qrToken.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED);
        }
        Coupon coupon = couponRepository.findByIdAndQrToken(couponId, qrToken)
            .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return CouponQrResponse.from(coupon);
    }

    private void issueQr(Coupon coupon) {
        String qrToken = UUID.randomUUID().toString();
        String payload = String.format("%s?couponId=%s&qrToken=%s",
            couponQrBaseUrl, coupon.getId(), qrToken);

        byte[] qrBytes = qrCodeGenerator.generatePngBytes(payload, 300);
        String key = "coupon/" + coupon.getId() + "/qr.png";
        String qrUrl = fileStoragePort.uploadFile(key, "image/png", qrBytes);

        coupon.attachQr(qrToken, qrUrl);
    }
}
