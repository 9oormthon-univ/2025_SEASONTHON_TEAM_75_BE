package com.trashheroesbe.feature.user.infrastructure;

import com.trashheroesbe.feature.user.domain.entity.UserDistrict;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDistrictRepository extends JpaRepository<UserDistrict, Long> {

    Boolean existsByUserIdAndDistrictId(Long userId, String districtId);

    @EntityGraph(attributePaths = {"district"})
    List<UserDistrict> findByUserId(Long userId);

    @Query("""
        SELECT ud 
        FROM UserDistrict ud 
        JOIN FETCH ud.district 
        WHERE ud.user.id = :userId
        """)
    List<UserDistrict> findByUserIdFetchJoin(Long userId);

}
