package com.trashheroesbe.feature.revision.infrastructure;

import com.trashheroesbe.feature.revision.domain.Revision;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {

    @EntityGraph("trashType")
    List<Revision> findAllByOrderByRevisionDateAsc();
}
