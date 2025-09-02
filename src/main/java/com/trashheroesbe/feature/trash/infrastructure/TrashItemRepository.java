package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashItemRepository extends JpaRepository<TrashItem, Long> {
    List<TrashItem> findByTrashTypeId(Long trashTypeId);
}
