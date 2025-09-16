package com.trashheroesbe.feature.badge.infrastructure;

import com.trashheroesbe.feature.badge.domain.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

}
