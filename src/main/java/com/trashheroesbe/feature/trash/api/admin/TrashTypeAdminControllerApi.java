package com.trashheroesbe.feature.trash.api.admin;

import com.trashheroesbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "TrashType", description = "쓰레기 타입 관련 API(Admin)")
public interface TrashTypeAdminControllerApi {

    @Operation(summary = "쓰레기 타입 initializer", description = "쓰레기 타입을 동기화 합니다.")
    ApiResponse<Void> initializeTrashType();

    @Operation(summary = "쓰레기 타입 이미지 업로드", description = "특정 쓰레기 타입에 이미지를 업로드합니다.")
    ApiResponse<Void> uploadTrashTypeImage(
        @Parameter(description = "쓰레기 타입 ID", required = true)  Long trashTypeId,
        @Parameter(description = "업로드할 이미지 파일", required = true)  MultipartFile image);

}
