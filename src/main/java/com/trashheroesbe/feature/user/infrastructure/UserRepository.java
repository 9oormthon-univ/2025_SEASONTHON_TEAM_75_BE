package com.trashheroesbe.feature.user.infrastructure;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.type.AuthProvider;
import com.trashheroesbe.feature.user.domain.type.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakaoId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        DELETE FROM User u 
        WHERE u.provider = :provider 
        AND u.role = :role 
        AND u.createdAt < :cutoffTime
        """)
    int deleteExpiredGuestUsers(@Param("provider") AuthProvider provider,
        @Param("role") Role role,
        @Param("cutoffTime") LocalDateTime cutoffTime);
}
