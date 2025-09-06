package com.trashheroesbe.feature.revision.api.admin;

import com.trashheroesbe.feature.revision.dto.request.RevisionCreateRequest;
import com.trashheroesbe.feature.revision.dto.response.RevisionListResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "RevisionAdmin", description = "개정 관련 API(admin)")
public interface RevisionAdminControllerApi {

    @Operation(summary = "쓰레기 개정 정보 저장하기", description = "쓰레기 개정 정보 저장합니다.")
    ApiResponse<Void> createRevision(RevisionCreateRequest request);
}
