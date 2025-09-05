package com.trashheroesbe.feature.rank.infrastructure;

import com.trashheroesbe.feature.rank.domain.TrashRank;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashRankRepository extends JpaRepository<TrashRank, Long> {

    @EntityGraph(attributePaths = "trashType")
    @Query("SELECT tr FROM TrashRank tr " +
        "WHERE tr.weekStartDate = :currentWeekStart " +
        "ORDER BY tr.rankOrder ASC")
    List<TrashRank> findCurrentWeekRanking(@Param("currentWeekStart") LocalDate currentWeekStart);
}
