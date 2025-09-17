package com.trashheroesbe.feature.badge.infrastructure;

import com.trashheroesbe.feature.badge.domain.entity.Badge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByName(String name);
}
