package com.trashheroesbe.feature.trash.api;


import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.dto.response.TrashResultResponse;
import com.trashheroesbe.global.auth.security.CustomerDetails;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TrashResultResponse.class),
                    examples = @ExampleObject(name = "success", value = """
{
  "httpCode": 200,
  "httpStatus": "OK",
  "message": "요청에 성공하였습니다.",
  "data": {
    "id": 8,
    "imageUrl": "https://trashheroes.s3.ap-northeast-2.amazonaws.com/trash/20250904_003905_126ed9a4.jpg",
    "name": "쓰레기",
    "summary": "생수용 PET 플라스틱 병(뚜껑 포함)",
    "itemName": "PET(투명 페트병)",
    "typeCode": "R04",
    "typeName": "PET(투명 페트병)",
    "guideSteps": [
      "STEP 1: 내용물을 비우고 깨끗이 헹궈요.",
      "STEP 2: 비닐 라벨을 제거하여 비닐류로 배출해요.",
      "STEP 3: 페트병은 찌그러뜨린 뒤 뚜껑을 닫아 일반 플라스틱류와 구분하여 투명/반투명 봉투에 담아 배출해요."
    ],
    "cautionNote": "주의: 유색·불투명 페트병이나 식용유병은 투명 페트병으로 분리하지 않아요.",
    "parts": [
      { "name": "페트병 뚜껑", "typeCode": "R04", "typeName": "PET(투명 페트병)" },
      { "name": "투명 페트병 몸체", "typeCode": "R04", "typeName": "PET(투명 페트병)" },
      { "name": "비닐 라벨", "typeCode": "R05", "typeName": "비닐류" }
    ],
    "createdAt": "2025-09-04T00:39:17.853269"
  }
}
""")
            )
    )
    ApiResponse<TrashResultResponse> createTrash(CreateTrashRequest request, @AuthenticationPrincipal CustomerDetails customerDetails);

    @Operation(summary = "쓰레기 조회", description = "쓰레기 ID로 상세(가이드/부품 포함) 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TrashResultResponse.class)
            )
    )
    ApiResponse<TrashResultResponse> getTrash(@PathVariable Long trashId);

    @Operation(summary = "내 쓰레기 목록", description = "현재 인증된 사용자의 모든 쓰레기를 최신순으로 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TrashResultResponse.class),
                    examples = @ExampleObject(name = "success", value = """
{
  "httpCode": 200,
  "httpStatus": "OK",
  "message": "요청에 성공하였습니다.",
  "data": [
    { "id": 8, "imageUrl": "...", "name": "쓰레기", "summary": "..." },
    { "id": 7, "imageUrl": "...", "name": "쓰레기", "summary": "..." }
  ]
}
""")
            )
    )
    ApiResponse<List<TrashResultResponse>> getMyTrash(@AuthenticationPrincipal CustomerDetails customerDetails);

    @Operation(summary = "쓰레기 삭제", description = "쓰레기와 해당 이미지(S3)를 함께 삭제합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
{
  "httpCode": 200,
  "httpStatus": "OK",
  "message": "요청에 성공하였습니다.",
  "data": null
}
""")
            )
    )
    ApiResponse<Void> deleteTrash(@PathVariable Long trashId, @AuthenticationPrincipal CustomerDetails customerDetails);
}