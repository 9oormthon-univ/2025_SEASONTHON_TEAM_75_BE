package com.trashheroesbe.feature.trash.api;


import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.dto.response.TrashResult;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Trash", description = "쓰레기 관련 API")
public interface TrashControllerApi {

    @Operation(
            summary = "쓰레기 생성",
            description = "현재 인증된 사용자의 이미지 파일로 쓰레기를 생성합니다. 이미지는 S3에 업로드되고 URL이 저장됩니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = CreateTrashRequest.class)
                    )
            )
    )
    ApiResponse<TrashResult> createTrash(CreateTrashRequest request, @AuthenticationPrincipal CustomerDetails customerDetails);

    @Operation(summary = "쓰레기 조회", description = "쓰레기 ID로 정보를 조회합니다.")
    ApiResponse<TrashResult> getTrash(@PathVariable Long trashId);

    @Operation(summary = "내 쓰레기 목록", description = "현재 인증된 사용자의 모든 쓰레기를 조회합니다.")
    ApiResponse<List<TrashResult>> getMyTrash(@AuthenticationPrincipal CustomerDetails customerDetails);
}