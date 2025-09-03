package com.trashheroesbe.feature.question.api;

import com.trashheroesbe.feature.trash.dto.response.TrashDescriptionResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashItemResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashTypeResponse;
import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Question", description = "질문 관련 API(챗봇)")
public interface QuestionControllerApi {

    @Operation(summary = "상위 쓰레기 카테고리 조회(쓰레기 타입 조회)", description = "쓰레기 카테고리를 조회합니다.(쓰레기 타입)")
    ApiResponse<List<TrashTypeResponse>> getTrashTypes();

    @Operation(summary = "쓰레기 카테고리별 품목 조회(쓰레기 품목 조회)", description = "trashTypeId를 통해 쓰레기 품목을 조회 합니다.")
    ApiResponse<List<TrashItemResponse>> getTrashItems(Long trashTypeId);

    @Operation(summary = "쓰레기 배출 방법 조회하기", description = "trashItemId를 통해 쓰레기 배출 방법을 조회 합니다.")
    ApiResponse<List<TrashDescriptionResponse>> getTrashDescriptions(Long trashItemId);
}
