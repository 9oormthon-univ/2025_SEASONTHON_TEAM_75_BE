package com.trashheroesbe.feature.badge.infrastructure;

import com.trashheroesbe.feature.badge.domain.entity.Badge;
import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import com.trashheroesbe.feature.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeProgressRepository extends JpaRepository<BadgeProgress, Long> {

    Optional<BadgeProgress> findByUserAndBadge(User user, Badge badge);
}
