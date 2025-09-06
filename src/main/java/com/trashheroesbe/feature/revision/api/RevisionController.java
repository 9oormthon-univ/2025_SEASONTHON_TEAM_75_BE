package com.trashheroesbe.feature.revision.api;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.revision.application.RevisionService;
import com.trashheroesbe.feature.revision.dto.response.RevisionListResponse;
import com.trashheroesbe.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/revisions")
public class RevisionController implements RevisionControllerApi {

    private final RevisionService revisionService;

    @Override
    @GetMapping
    public ApiResponse<List<RevisionListResponse>> getRevisionList() {
        List<RevisionListResponse> response = revisionService.getRevisionList();
        return ApiResponse.success(OK, response);
    }
}
