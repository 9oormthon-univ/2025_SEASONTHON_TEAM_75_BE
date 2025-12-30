package com.trashheroesbe.feature.partner.api;

import com.trashheroesbe.feature.partner.dto.request.RegisterPartnerRequest;
import com.trashheroesbe.feature.partner.dto.request.UpdatePartnerRequest;
import com.trashheroesbe.feature.partner.dto.response.RegisterPartnerResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Partner", description = "파트너 관련 API")
public interface PartnerControllerApi {

    @Operation(summary = "파트너 회원가입", description = "파트너 회원가입을 진행합니다.")
    ApiResponse<RegisterPartnerResponse> registerPartner(

        @RequestPart(value = "metadata")
        RegisterPartnerRequest request,

        @Parameter(description = "업로드할 이미지 파일")
        @RequestPart(value = "image", required = false)
        MultipartFile image
    );

    @Operation(summary = "파트너 정보 수정", description = "파트너 정보를 수정합니다.")
    ApiResponse<Void> updatePartner(
        @RequestPart(value = "metadata")
        UpdatePartnerRequest request,

        @Parameter(description = "업로드할 이미지 파일")
        @RequestPart(value = "image", required = false)
        MultipartFile image,

        @Parameter(hidden = true)
        CustomerDetails customerDetails

    );
}
