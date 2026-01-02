package com.trashheroesbe.feature.partner.dto.response;

import com.trashheroesbe.feature.partner.domain.entity.Partner;

public record PartnerStoreResponse(
    Long partnerId,
    String partnerName,
    String address,
    String description,
    String imageUrl
) {

    public static PartnerStoreResponse from(Partner partner) {
        return new PartnerStoreResponse(
            partner.getId(),
            partner.getPartnerName(),
            partner.getAddress(),
            partner.getDescription(),
            partner.getImageUrl()
        );
    }
}
