package com.trashheroesbe.feature.search.infrastructure;

import com.trashheroesbe.feature.search.domain.SearchLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    @Query("""
        SELECT sl.trashType, COUNT(sl)
                FROM SearchLog sl
                WHERE sl.createdAt BETWEEN :startTime AND :endTime
                GROUP BY sl.trashType
                ORDER BY COUNT(sl) DESC
        """)
    List<Object[]> countSearchLogsByTrashTypeAndDateRange(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
