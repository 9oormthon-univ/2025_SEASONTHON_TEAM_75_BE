package com.trashheroesbe.feature.trash.api;

import com.trashheroesbe.feature.trash.application.TrashService;
import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.application.TrashCreateUseCase;
import com.trashheroesbe.feature.trash.dto.response.TrashResultResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.response.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trash")
public class TrashController implements TrashControllerApi {

    private final TrashCreateUseCase trashCreateUseCase;
    private final TrashService trashService;

    @Override
    @PostMapping
    public ApiResponse<TrashResultResponse> createTrash(
            @ModelAttribute CreateTrashRequest request,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        log.info("쓰레기 생성 요청: userId={}, fileName={}",
                customerDetails.getUser().getId(), request.imageFile().getOriginalFilename());

        TrashResultResponse result = trashCreateUseCase.createTrash(request, customerDetails.getUser());
        return ApiResponse.success(SuccessCode.OK, result);
    }

    @Override
    @GetMapping("/{trashId}")
    public ApiResponse<TrashResultResponse> getTrash(@PathVariable Long trashId) {
        // TODO: 조회 로직 구현 필요
        return null;
    }

    @Override
    @GetMapping("/my")
    public ApiResponse<List<TrashResultResponse>> getMyTrash(
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        log.info("내 쓰레기 목록 조회 요청: userId={}", customerDetails.getUser().getId());
        
        List<TrashResultResponse> myTrashList = trashService.getTrashByUser(customerDetails.getUser());
        
        return ApiResponse.success(SuccessCode.OK, myTrashList);
    }

    @Override
    @DeleteMapping("/{trashId}")
    public ApiResponse<Void> deleteTrash(
            @PathVariable Long trashId,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        log.info("쓰레기 삭제 요청: userId={}, trashId={}", customerDetails.getUser().getId(), trashId);
        trashService.deleteTrash(trashId, customerDetails.getUser());
        return ApiResponse.success(SuccessCode.OK);
    }

}