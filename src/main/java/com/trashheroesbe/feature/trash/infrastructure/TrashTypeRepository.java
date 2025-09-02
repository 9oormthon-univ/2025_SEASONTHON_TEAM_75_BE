package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashTypeRepository extends JpaRepository<TrashType, Long> {

}
