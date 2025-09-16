package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.entity.Part;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PartRepository extends JpaRepository<Part, Long> {
    Optional<Part> findByTrashTypeAndName(TrashType trashType, String name);
}