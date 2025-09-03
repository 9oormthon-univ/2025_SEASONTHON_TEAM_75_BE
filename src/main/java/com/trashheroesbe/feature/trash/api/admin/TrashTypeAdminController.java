package com.trashheroesbe.feature.trash.api.admin;


import com.trashheroesbe.feature.trash.application.admin.TrashTypeAdminService;
import com.trashheroesbe.global.response.ApiResponse;
import com.trashheroesbe.global.response.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
