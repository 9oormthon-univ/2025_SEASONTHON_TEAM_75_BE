package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashItemRepository extends JpaRepository<TrashItem, Long> {

    @EntityGraph(attributePaths = "trashType")
    List<TrashItem> findByTrashTypeId(Long trashTypeId);

    Optional<TrashItem> findByTrashTypeAndName(TrashType trashType, String name);

    @EntityGraph(attributePaths = {"trashType"})
    List<TrashItem> findByTrashType_Type(Type type);
}