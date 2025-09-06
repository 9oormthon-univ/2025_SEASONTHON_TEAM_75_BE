package com.trashheroesbe.feature.disposal.infrastructure;

import com.trashheroesbe.feature.disposal.domain.Disposal;
import com.trashheroesbe.feature.district.domain.entity.District;
import com.trashheroesbe.feature.trash.domain.type.Type;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DisposalRepository extends JpaRepository<Disposal, Long> {

    @EntityGraph(attributePaths = {"district", "trashType"})
    Optional<Disposal> findByDistrict_IdAndTrashType_Type(String districtId, Type type);

    @EntityGraph(attributePaths = {"district", "trashType"})
    List<Disposal> findByDistrict(District district);

    @EntityGraph(attributePaths = {"district", "trashType"})
    @Query("""
        select d
        from Disposal d
        where d.district.id = :districtId
          and function('json_overlaps', d.days, function('json_array', :day)) = true
        """)
    List<Disposal> findAllByDistrictAndDay(
        @Param("districtId") String districtId,
        @Param("day") String day
    );
}