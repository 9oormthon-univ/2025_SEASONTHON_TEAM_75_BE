package com.trashheroesbe.feature.coupon.infrastructure;

import com.trashheroesbe.feature.coupon.domain.entity.Coupon;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select c from Coupon c join fetch c.partner where c.id = :id")
    Optional<Coupon> findByIdFetchPartner(@Param("id") Long id);

    @Query("select c from Coupon c join fetch c.partner p where p.id = :partnerId")
    List<Coupon> findAllByPartnerIdFetch(@Param("partnerId") Long partnerId);
}
