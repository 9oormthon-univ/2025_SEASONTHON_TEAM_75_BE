package com.trashheroesbe.feature.disposal.infrastructure;

import com.trashheroesbe.feature.disposal.domain.Disposal;
import com.trashheroesbe.feature.district.domain.entity.District;
import com.trashheroesbe.feature.trash.domain.Type;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DisposalRepository extends JpaRepository<Disposal, Long> {

    // 1) 기존 용도: 자치구+타입(enum)으로 단건 days 조회
    @EntityGraph(attributePaths = {"district", "trashType"})
    Optional<Disposal> findByDistrict_IdAndTrashType_Type(String districtId, Type type);

    // 2) 추가 용도: 자치구로 전체 가져와서 서비스에서 타입 필터링이 필요할 때
    @EntityGraph(attributePaths = {"district", "trashType"})
    List<Disposal> findByDistrict(District district);

    // 3) 추가 용도: 자치구의 특정 요일 배출 항목들 조회(MySQL 8+ 전제)
    @EntityGraph(attributePaths = {"district", "trashType"})
    @Query("""
        select d
        from Disposal d
        where d.district.id = :districtId
          and function('json_overlaps', d.days, function('json_array', :day)) = true
        """)
    List<Disposal> findAllByDistrictAndDay(@Param("districtId") String districtId,
                                           @Param("day") String day);
}