package com.trashheroesbe.global.util;

public final class CouponQrUtil {

    private static final String COUPON_QR_KEY_FORMAT = "coupon/%s/qr.png";
    private static final String COUPON_QR_PAYLOAD_FORMAT = "%s?couponId=%s&qrToken=%s";

    private CouponQrUtil() {
    }

    public static String buildPayload(String baseUrl, Long couponId, String qrToken) {
        return String.format(COUPON_QR_PAYLOAD_FORMAT, baseUrl, couponId, qrToken);
    }

    public static String buildKey(Long couponId) {
        return String.format(COUPON_QR_KEY_FORMAT, couponId);
    }
}

