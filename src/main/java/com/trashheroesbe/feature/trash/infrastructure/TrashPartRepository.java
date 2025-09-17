package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.entity.Part;
import com.trashheroesbe.feature.trash.domain.entity.TrashPart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface TrashPartRepository extends JpaRepository<TrashPart, Long> {

    @Query("select tp.part from TrashPart tp join tp.part p join p.trashType tt where tp.trash.id = :trashId")
    List<Part> findPartsByTrashId(Long trashId);
}