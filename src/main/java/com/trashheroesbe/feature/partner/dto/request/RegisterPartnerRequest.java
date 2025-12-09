package com.trashheroesbe.feature.partner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterPartnerRequest(

    @Schema(description = "브랜드명", example = "어스어스")
    @NotBlank(message = "브랜드명은 필수입니다.")
    String partnerName,

    @Schema(description = "이메일", example = "usus@gmail.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email,

    @Schema(description = "주소", example = "서울 마포구 어울마당로2길 10 1층 초록문")
    @NotBlank(message = "주소는 필수입니다.")
    String address,

    @Schema(description = "설명", example = "버려지는 것들에 새로운 가치를 더해 새활용하는 업사이클링 브랜드")
    String description
) {

}
