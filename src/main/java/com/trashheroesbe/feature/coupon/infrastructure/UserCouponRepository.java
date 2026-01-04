package com.trashheroesbe.feature.coupon.infrastructure;

import com.trashheroesbe.feature.coupon.domain.entity.UserCoupon;
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
    Optional<UserCoupon> findByIdAndQrTokenFetchAll(@Param("id") Long id, @Param("qrToken") String qrToken);
}
