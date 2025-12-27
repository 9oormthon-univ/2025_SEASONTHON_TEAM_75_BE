package com.trashheroesbe.feature.point.infrastructure;

import com.trashheroesbe.feature.point.domain.entity.UserPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointHistoryRepository extends JpaRepository<UserPointHistory, Long> {

}
