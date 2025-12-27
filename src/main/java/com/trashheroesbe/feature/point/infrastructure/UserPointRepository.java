package com.trashheroesbe.feature.point.infrastructure;

import com.trashheroesbe.feature.point.domain.entity.UserPoint;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT up FROM UserPoint up WHERE up.user.id = :userId")
    Optional<UserPoint> findByUserIdWithLock(@Param("userId") Long userId);
}
