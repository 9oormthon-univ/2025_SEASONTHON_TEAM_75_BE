package com.trashheroesbe.feature.disposal.infrastructure;

import com.trashheroesbe.feature.disposal.domain.Disposal;
import com.trashheroesbe.feature.district.domain.entity.District;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DisposalRepository extends CrudRepository<Disposal, Long> {

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
