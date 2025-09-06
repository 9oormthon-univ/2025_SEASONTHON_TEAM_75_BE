package com.trashheroesbe.feature.revision.api.admin;

import static com.trashheroesbe.global.response.type.SuccessCode.OK;

import com.trashheroesbe.feature.revision.api.RevisionControllerApi;
import com.trashheroesbe.feature.revision.application.RevisionAdminService;
import com.trashheroesbe.feature.revision.dto.request.RevisionCreateRequest;
import com.trashheroesbe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/revisions")
public class RevisionAdminController implements RevisionAdminControllerApi {

    private final RevisionAdminService revisionAdminService;

    @Override
    public ApiResponse<Void> createRevision(RevisionCreateRequest request) {
        revisionAdminService.createRevision(request);
        return ApiResponse.success(OK);
    }
}
