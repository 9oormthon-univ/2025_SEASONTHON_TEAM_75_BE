package com.trashheroesbe.feature.trash.application;

import com.trashheroesbe.feature.trash.dto.request.CreateTrashRequest;
import com.trashheroesbe.feature.trash.dto.response.TrashResult;
import com.trashheroesbe.feature.user.domain.User;

public interface TrashCreateUseCase {

    TrashResult createTrash(CreateTrashRequest request, User user);
}