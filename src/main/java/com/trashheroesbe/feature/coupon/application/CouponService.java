package com.trashheroesbe.feature.coupon.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponQrResponse;
import com.trashheroesbe.feature.coupon.infrastructure.CouponRepository;
import com.trashheroesbe.feature.partner.domain.entity.Partner;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.qrcode.QrCodeGenerator;
import com.trashheroesbe.global.util.CouponQrUtil;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import com.trashheroesbe.global.response.type.ErrorCode;
import com.trashheroesbe.global.auth.security.CustomerDetails;
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

    public CouponCreateResponse createCoupon(
        CustomerDetails customerDetails,
        CouponCreateRequest request
    ) {
        Partner partner = extractPartner(customerDetails);
        Coupon saved = couponRepository.save(Coupon.create(request, partner));

        String qrToken = UUID.randomUUID().toString();
        String payload = CouponQrUtil.buildPayload(couponQrBaseUrl, saved.getId(), qrToken);
        byte[] qrBytes = qrCodeGenerator.generatePngBytes(payload, 300);
        String key = CouponQrUtil.buildKey(saved.getId());
        String qrUrl = fileStoragePort.uploadFile(key, "image/png", qrBytes);
        saved.attachQr(qrToken, qrUrl);

        return CouponCreateResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public CouponQrResponse findByQr(Long couponId, String qrToken) {
        if (couponId == null || qrToken == null || qrToken.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED);
        }
        Coupon coupon = couponRepository.findByIdAndQrToken(couponId, qrToken)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        return CouponQrResponse.from(coupon);
    }

    private Partner extractPartner(CustomerDetails customerDetails) {
        if (customerDetails == null || customerDetails.getUser() == null) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED_EXCEPTION);
        }
        Partner partner = customerDetails.getUser().getPartner();
        if (partner == null) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        return partner;
    }
}
