package com.trashheroesbe.feature.trash.api.admin;

import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TrashType", description = "쓰레기 타입 관련 API(Admin)")
public interface TrashTypeAdminControllerApi {

    @Operation(summary = "쓰레기 타입 initializer", description = "쓰레기 타입을 동기화 합니다.")
    ApiResponse<Void> initializeTrashType();
}
