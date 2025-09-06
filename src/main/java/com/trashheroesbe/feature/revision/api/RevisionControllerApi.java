package com.trashheroesbe.feature.revision.api;

import com.trashheroesbe.feature.revision.dto.response.RevisionListResponse;
import com.trashheroesbe.feature.revision.dto.response.RevisionResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Revision", description = "개정 관련 API")
public interface RevisionControllerApi {

    @Operation(summary = "쓰레기 개정 전체 조회", description = "쓰레기 개정 정보를 전체 조회합니다.")
    ApiResponse<List<RevisionListResponse>> getRevisionList();

    @Operation(summary = "쓰레기 개정 상세 조회", description = "쓰레기 개정 정보를 상세 조회합니다.")
    ApiResponse<RevisionResponse> getRevision(Long revisionId);

}
