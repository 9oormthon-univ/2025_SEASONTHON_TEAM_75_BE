package com.trashheroesbe.feature.coupon.infrastructure;

import com.trashheroesbe.feature.coupon.domain.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByIdAndQrToken(Long id, String qrToken);
}
