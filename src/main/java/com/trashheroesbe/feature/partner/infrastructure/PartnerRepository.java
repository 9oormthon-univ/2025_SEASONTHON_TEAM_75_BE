package com.trashheroesbe.feature.partner.infrastructure;

import com.trashheroesbe.feature.partner.domain.entity.Partner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    boolean existsByEmail(String email);

    Optional<Partner> findByEmail(String email);
}
