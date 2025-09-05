package com.trashheroesbe.feature.search.infrastructure;

import com.trashheroesbe.feature.search.domain.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

}
