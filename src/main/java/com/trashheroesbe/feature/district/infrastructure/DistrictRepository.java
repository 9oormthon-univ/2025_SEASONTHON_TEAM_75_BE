package com.trashheroesbe.feature.district.infrastructure;

import com.trashheroesbe.feature.district.domain.entity.District;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {

    @Query("""
        SELECT d
        FROM District d
        WHERE d.sido LIKE CONCAT('%', :sido, '%')
          AND d.sigungu IS NOT NULL
          AND d.eupmyeondong IS NULL
        ORDER BY d.sigungu ASC
        """)
    List<District> findDistrictsBySido(@Param("sido") String sido);

    @Query("""
        SELECT d
        FROM District d
        WHERE d.sido LIKE CONCAT('%', :sido, '%')
          AND d.sigungu LIKE CONCAT('%', :sigungu, '%')
          AND d.eupmyeondong IS NOT NULL
        ORDER BY d.eupmyeondong ASC
        """)
    List<District> findEupmyeondongBySidoAndSigungu(
        @Param("sido") String sido,
        @Param("sigungu") String sigungu
    );
}
