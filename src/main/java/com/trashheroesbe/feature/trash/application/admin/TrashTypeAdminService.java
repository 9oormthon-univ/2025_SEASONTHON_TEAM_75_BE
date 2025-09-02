package com.trashheroesbe.feature.trash.application.admin;

import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrashTypeAdminService {

    private final TrashTypeRepository trashTypeRepository;

    public void initializeTrashType() {
        List<TrashType> trashTypes = TrashType.initialize();
        if (!trashTypeRepository.isEmpty()) {
            trashTypeRepository.deleteAllInBatch();
        }
        trashTypeRepository.saveAll(trashTypes);
    }
}
