package com.trashheroesbe.feature.trash.application.admin;

import com.trashheroesbe.feature.trash.domain.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TrashTypeAdminService {

    private final TrashTypeRepository trashTypeRepository;

    public void initializeTrashType() {
        for (var type : Type.values()) {
            trashTypeRepository.findByType(type)
                .orElseGet(() -> trashTypeRepository.save(TrashType.of(type)));
        }
    }
}
