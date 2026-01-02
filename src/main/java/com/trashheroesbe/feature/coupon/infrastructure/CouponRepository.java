package com.trashheroesbe.feature.coupon.infrastructure;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @EntityGraph(attributePaths = {"partner"})
    Optional<Coupon> findByIdAndQrToken(Long id, String qrToken);

    @Query("select c from Coupon c join fetch c.partner where c.id = :id")
    Optional<Coupon> findByIdFetchPartner(@Param("id") Long id);
}
