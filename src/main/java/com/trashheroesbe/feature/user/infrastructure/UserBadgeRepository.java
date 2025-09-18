package com.trashheroesbe.feature.user.infrastructure;

import com.trashheroesbe.feature.badge.domain.entity.Badge;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.entity.UserBadge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    Boolean existsByUserAndBadge(User user, Badge badge);

    List<UserBadge> findByUserOrderByEarnedAtAsc(User user);
}
