package com.trashheroesbe.feature.partner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdatePartnerRequest(
    @Schema(description = "파트너 이름", example = "어스어스2")
    String partnerName,

    @Schema(description = "이메일", example = "usus2@gmailc.om")
    @Email
    String email,

    @Schema(description = "비밀번호", example = "123123qwe!")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#^()_+=\\-\\[\\]{}|;:'\",.<>/?])[a-z\\d@$!%*?&#^()_+=\\-\\[\\]{}|;:'\",.<>/?]{8,}$",
        message = "비밀번호는 소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
    )
    String password,

    @Schema(description = "주소", example = "서울 서대문구 창전동")
    String address,

    @Schema(description = "설명", example = "우리는 어스어스입니다!!")
    String description
) {

}
