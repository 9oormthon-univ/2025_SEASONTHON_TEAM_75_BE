package com.trashheroesbe.feature.partner.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.partner.application.PartnerService;
import com.trashheroesbe.feature.partner.dto.request.RegisterPartnerRequest;
import com.trashheroesbe.feature.partner.dto.request.UpdatePartnerRequest;
import com.trashheroesbe.feature.partner.dto.response.RegisterPartnerResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partners")
public class PartnerController implements PartnerControllerApi {

    private final PartnerService partnerService;

    @Override
    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RegisterPartnerResponse> registerPartner(
        @RequestPart(value = "metadata") @Valid RegisterPartnerRequest request,
        @RequestPart(value = "image", required = true) MultipartFile image
    ) {
        RegisterPartnerResponse response = partnerService.registerPartner(request, image);
        return ApiResponse.success(OK, response);
    }

    @Override
    @PatchMapping()
    public ApiResponse<Void> updatePartner(
        @RequestPart(value = "metadata") @Valid UpdatePartnerRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        partnerService.updatePartner(request, image, customerDetails.getUser());
        return ApiResponse.success(OK);
    }
}
