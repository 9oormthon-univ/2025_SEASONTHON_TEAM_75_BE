package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.entity.TrashDescription;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrashDescriptionRepository extends JpaRepository<TrashDescription, Long> {
    Optional<TrashDescription> findByTrashType(TrashType trashType);
}