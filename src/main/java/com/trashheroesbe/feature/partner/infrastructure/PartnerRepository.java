package com.trashheroesbe.feature.partner.infrastructure;

import com.trashheroesbe.feature.partner.domain.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

}
