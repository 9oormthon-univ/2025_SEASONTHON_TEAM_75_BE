package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashTypeRepository extends JpaRepository<TrashType, Long> {

    @Query("select count(a) = 0 from TrashType a")
    boolean isEmpty();

    Optional<TrashType> findByType(Type type);

    List<TrashType> findAllByOrderByIdAsc();

    @Query("select t.type from TrashType t")
    java.util.List<Type> findAllTypes();
}