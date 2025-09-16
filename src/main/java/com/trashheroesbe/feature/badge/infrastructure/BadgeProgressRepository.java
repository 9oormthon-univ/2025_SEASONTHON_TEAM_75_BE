package com.trashheroesbe.feature.badge.infrastructure;

import com.trashheroesbe.feature.badge.domain.entity.BadgeProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeProgressRepository extends JpaRepository<BadgeProgress, Long> {

}
