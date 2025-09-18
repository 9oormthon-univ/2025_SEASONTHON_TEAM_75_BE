package com.trashheroesbe.feature.trash.infrastructure;

import com.trashheroesbe.feature.trash.domain.entity.Trash;
import com.trashheroesbe.feature.user.domain.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrashRepository extends JpaRepository<Trash, Long> {

    List<Trash> findByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT t FROM Trash t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
    List<Trash> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    List<Trash> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
}
