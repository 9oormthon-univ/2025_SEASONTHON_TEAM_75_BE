package com.trashheroesbe.feature.trash.domain.service;

import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashTypeFinder {

    private final TrashTypeRepository trashTypeRepository;

    public List<TrashType> getAllTrashTypes() {
        return trashTypeRepository.findAll();
    }
}
