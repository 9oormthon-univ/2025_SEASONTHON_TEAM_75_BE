package com.trashheroesbe.feature.trash.application;

import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.dto.response.TrashResultResponse;
import com.trashheroesbe.feature.user.domain.entity.User;

public interface TrashCreateUseCase {

    TrashResultResponse createTrash(CreateTrashRequest request, User user);
}