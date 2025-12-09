package com.trashheroesbe.feature.partner.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.partner.application.PartnerService;
import com.trashheroesbe.feature.partner.dto.request.RegisterPartnerRequest;
import com.trashheroesbe.feature.partner.dto.response.RegisterPartnerResponse;
import com.trashheroesbe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
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
        @RequestPart(value = "metadata") RegisterPartnerRequest request,
        @RequestPart(value = "image", required = true) MultipartFile image
    ) {
        RegisterPartnerResponse response = partnerService.registerPartner(request, image);
        return ApiResponse.success(OK, response);
    }
}
