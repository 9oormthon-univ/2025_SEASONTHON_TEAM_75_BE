package com.trashheroesbe.feature.coupon.application;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import com.trashheroesbe.feature.coupon.dto.request.CouponCreateRequest;
import com.trashheroesbe.feature.coupon.dto.request.CouponUpdateRequest;
import com.trashheroesbe.feature.coupon.dto.response.CouponCreateResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponUsageStatisticsResponse;
import com.trashheroesbe.feature.coupon.dto.response.CouponUsageStatisticsResponse.UsageSummary;
import com.trashheroesbe.feature.coupon.dto.response.CouponUsageStatisticsResponse.UsageSummary.PeriodRange;
import com.trashheroesbe.feature.coupon.dto.response.PartnerCouponResponse;
import com.trashheroesbe.feature.coupon.infrastructure.CouponRepository;
import com.trashheroesbe.feature.coupon.infrastructure.UserCouponRepository;
import com.trashheroesbe.feature.partner.domain.entity.Partner;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.response.type.ErrorCode;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.util.DateTimeUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional(readOnly = true)
    public List<PartnerCouponResponse> getPartnerCoupons(CustomerDetails customerDetails) {
        Partner partner = extractPartner(customerDetails);
        return couponRepository.findAllByPartnerIdFetch(partner.getId()).stream()
            .map(PartnerCouponResponse::from)
            .toList();
    }

    public CouponCreateResponse createCoupon(
        CustomerDetails customerDetails,
        CouponCreateRequest request
    ) {
        Partner partner = extractPartner(customerDetails);
        Coupon saved = couponRepository.save(Coupon.create(request, partner));

        return CouponCreateResponse.from(saved);
    }

    @Transactional
    public void deleteCoupon(CustomerDetails customerDetails, Long couponId) {
        Partner partner = extractPartner(customerDetails);
        Coupon coupon = couponRepository.findByIdFetchPartner(couponId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        if (!coupon.getPartner().getId().equals(partner.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED_EXCEPTION);
        }
        couponRepository.delete(coupon);
    }

    @Transactional
    public CouponCreateResponse updateCoupon(
        CustomerDetails customerDetails,
        Long couponId,
        CouponUpdateRequest request
    ) {
        Partner partner = extractPartner(customerDetails);
        Coupon coupon = couponRepository.findByIdFetchPartner(couponId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        if (!coupon.getPartner().getId().equals(partner.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED_EXCEPTION);
        }
        coupon.applyUpdate(
            request.title(),
            request.content(),
            request.type(),
            request.pointCost(),
            request.discountType(),
            request.discountValue(),
            request.totalStock(),
            request.isActive()
        );
        return CouponCreateResponse.from(coupon);
    }

    @Transactional(readOnly = true)
    public CouponUsageStatisticsResponse getCouponUsage(CustomerDetails customerDetails) {
        Partner partner = extractPartner(customerDetails);

        LocalDate today = DateTimeUtils.getTodayDate();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime weekStart = DateTimeUtils.getWeekStart(today).atStartOfDay();
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();

        long weekCount = userCouponRepository.countUsedByPartnerIdAndUsedAtAfter(
            partner.getId(), weekStart);
        long monthCount = userCouponRepository.countUsedByPartnerIdAndUsedAtAfter(
            partner.getId(), monthStart);
        long totalCount = userCouponRepository.countByCouponPartnerIdAndStatus(
            partner.getId(), CouponStatus.USED);

        return new CouponUsageStatisticsResponse(
            new UsageSummary(totalCount, null),
            new UsageSummary(weekCount, new PeriodRange(weekStart, now)),
            new UsageSummary(monthCount, new PeriodRange(monthStart, now))
        );
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
