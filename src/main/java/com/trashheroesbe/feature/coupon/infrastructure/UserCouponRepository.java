package com.trashheroesbe.feature.coupon.infrastructure;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @EntityGraph(attributePaths = {"coupon", "coupon.partner"})
    List<UserCoupon> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"coupon", "coupon.partner"})
    Optional<UserCoupon> findById(Long id);
}
