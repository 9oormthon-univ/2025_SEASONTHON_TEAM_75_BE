package com.trashheroesbe.feature.coupon.infrastructure;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import com.trashheroesbe.feature.coupon.domain.type.CouponStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @EntityGraph(attributePaths = {"coupon", "coupon.partner"})
    List<UserCoupon> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"coupon", "coupon.partner"})
    Optional<UserCoupon> findWithDetailsById(Long id);

    @Query("select uc from UserCoupon uc "
        + "join fetch uc.user u "
        + "join fetch uc.coupon c "
        + "join fetch c.partner p "
        + "where uc.id = :id and uc.qrToken = :qrToken")
    Optional<UserCoupon> findByIdAndQrTokenFetchAll(@Param("id") Long id,
        @Param("qrToken") String qrToken);

    @Query("SELECT COUNT(uc) FROM UserCoupon uc " +
        "WHERE uc.coupon.partner.id = :partnerId " +
        "AND uc.status = 'USED' " +
        "AND uc.usedAt >= :from")
    long countUsedByPartnerIdAndUsedAtAfter(
        @Param("partnerId") Long partnerId,
        @Param("from") LocalDateTime from
    );

    long countByCouponPartnerIdAndStatus(
        @Param("partnerId") Long partnerId,
        @Param("status") CouponStatus status
    );

    @Query("SELECT uc FROM UserCoupon uc " +
        "JOIN FETCH uc.coupon c " +
        "JOIN FETCH uc.user u " +
        "WHERE c.partner.id = :partnerId " +
        "AND uc.status = 'USED' " +
        "ORDER BY uc.usedAt DESC")
    List<UserCoupon> findUsedByPartnerId(@Param("partnerId") Long partnerId);
}
