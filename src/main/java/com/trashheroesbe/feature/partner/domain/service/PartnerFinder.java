package com.trashheroesbe.feature.partner.domain.service;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.partner.domain.entity.Partner;
import com.trashheroesbe.feature.partner.infrastructure.PartnerRepository;
import com.trashheroesbe.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerFinder {

    private final PartnerRepository partnerRepository;

    public Partner findByEmail(String email) {
        return partnerRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
    }
}
