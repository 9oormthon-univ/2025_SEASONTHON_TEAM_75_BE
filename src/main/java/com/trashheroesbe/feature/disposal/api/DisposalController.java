package com.trashheroesbe.feature.disposal.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.disposal.application.DisposalService;
import com.trashheroesbe.feature.disposal.dto.response.DistrictTrashDisposalResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/disposals")
public class DisposalController implements DisposalControllerApi {

    private final DisposalService disposalService;

    @Override
    @GetMapping("/today")
    public ApiResponse<List<DistrictTrashDisposalResponse>> getTodayTrashDisposal(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<DistrictTrashDisposalResponse> response = disposalService.getTodayTrashDisposal(
            customerDetails.getUser().getId());
        return ApiResponse.success(OK, response);
    }
}
