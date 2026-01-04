package com.trashheroesbe.global.util;

public final class UserCouponQrUtil {

    private static final String USER_COUPON_QR_KEY_FORMAT = "user-coupon/%s/qr.png";
    private static final String USER_COUPON_QR_PAYLOAD_FORMAT = "%s?userCouponId=%s&qrToken=%s";

    private UserCouponQrUtil() {
    }

    public static String buildPayload(String baseUrl, Long userCouponId, String qrToken) {
        return String.format(USER_COUPON_QR_PAYLOAD_FORMAT, baseUrl, userCouponId, qrToken);
    }

    public static String buildKey(Long userCouponId) {
        return String.format(USER_COUPON_QR_KEY_FORMAT, userCouponId);
    }
}

