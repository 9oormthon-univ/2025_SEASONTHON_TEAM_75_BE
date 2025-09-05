package com.trashheroesbe.feature.trash.api.admin;


import com.trashheroesbe.feature.trash.application.admin.TrashTypeAdminService;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.response.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/trash-types")
public class TrashTypeAdminController implements TrashTypeAdminControllerApi {

    private final TrashTypeAdminService trashTypeAdminService;

    @Override
    @PostMapping("/initialize")
    public ApiResponse<Void> initializeTrashType() {
        trashTypeAdminService.initializeTrashType();
        return ApiResponse.success(SuccessCode.OK);
    }

    @Override
    @PostMapping("/{trashTypeId}/image")
    public ApiResponse<Void> uploadTrashTypeImage(
        @PathVariable Long trashTypeId,
        @RequestParam("image") MultipartFile image
    ) {
        trashTypeAdminService.uploadTrashTypeImage(trashTypeId, image);
        return ApiResponse.success(SuccessCode.OK);
    }


}
