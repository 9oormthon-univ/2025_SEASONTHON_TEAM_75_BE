package com.trashheroesbe.feature.partner.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record RegisterPartnerResponse(
    @Schema(description = "파트너 명", example = "어스어스")
    String partnerName,

    @Schema(description = "Email", example = "usus@gmail.com")
    String email,

    @Schema(description = "password", example = "1234")
    String password
) {

    public static RegisterPartnerResponse from(String partnerName, String email, String password) {
        return RegisterPartnerResponse.builder()
            .partnerName(partnerName)
            .email(email)
            .password(password)
            .build();
    }
}
