package com.trashheroesbe.feature.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginPartnerRequest(
    @Schema(description = "이메일", example = "usus@gmail.com")
    @Email
    @NotBlank(message = "이메일 입력은 필수입니다.")
    String email,

    @Schema(description = "패스워드", example = "usus123!")
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    String password
) {

}
