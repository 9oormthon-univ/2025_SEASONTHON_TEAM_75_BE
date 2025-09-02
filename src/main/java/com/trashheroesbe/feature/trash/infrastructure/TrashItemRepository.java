package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.TrashItem;
import com.trashheroesbe.feature.trash.domain.TrashType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrashItemRepository extends JpaRepository<TrashItem, Long> {
    Optional<TrashItem> findByTrashTypeAndName(TrashType type, String name);
}