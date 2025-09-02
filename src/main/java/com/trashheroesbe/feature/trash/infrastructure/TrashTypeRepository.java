package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.TrashType;
import com.trashheroesbe.feature.trash.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrashTypeRepository extends JpaRepository<TrashType, Long> {
    Optional<TrashType> findByType(Type type);

}
