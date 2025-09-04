package com.trashheroesbe.feature.disposal.infrastructure;

import com.trashheroesbe.feature.disposal.domain.Disposal;
import com.trashheroesbe.feature.trash.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DisposalRepository extends JpaRepository<Disposal, Long> {
    Optional<Disposal> findByDistrict_IdAndTrashType_Type(String districtId, Type type);
}