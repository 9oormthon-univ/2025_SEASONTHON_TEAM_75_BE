package com.trashheroesbe.feature.question.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.question.application.QuestionService;
import com.trashheroesbe.feature.trash.dto.response.TrashDescriptionResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashItemResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashTypeResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController implements QuestionControllerApi {

    private final QuestionService questionService;

    @Override
    @GetMapping("/trash-types")
    public ApiResponse<List<TrashTypeResponse>> getTrashTypes() {
        List<TrashTypeResponse> response = questionService.getTrashTypes();
        return ApiResponse.success(OK, response);
    }

    @Override
    @GetMapping("/trash-types/{trashTypeId}")
    public ApiResponse<List<TrashItemResponse>> getTrashItems(
        @PathVariable Long trashTypeId
    ) {
        List<TrashItemResponse> response = questionService.getTrashItems(trashTypeId);
        return ApiResponse.success(OK, response);
    }

    @Override
    @GetMapping("/trash-types/{trashTypeId}/descriptions")
    public ApiResponse<TrashDescriptionResponse> getTrashDescriptions(
        @PathVariable Long trashTypeId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        // TODO : 여기서 question객체 생성해서 검색기록 추적할 수 있도록 하면 좋을 거 같음
        TrashDescriptionResponse response = questionService.getTrashDescriptions(
            trashTypeId, customerDetails.getUser());
        return ApiResponse.success(OK, response);
    }

    @Override
    @GetMapping("/search")
    public ApiResponse<TrashDescriptionResponse> searchTrashDescription(
        @RequestParam("keyword") String keyword,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        TrashDescriptionResponse response = questionService.searchTrashDescription(
            keyword, customerDetails.getUser());
        return ApiResponse.success(OK, response);
    }


}
