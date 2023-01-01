package com.EntertainmentViet.backend.features.scoresystem.boundary;

import com.EntertainmentViet.backend.features.scoresystem.dao.ScoreTypeRepository;
import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreTypeDto;
import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreTypeService implements ScoreTypeBoundary {

    private final ScoreTypeMapper scoreTypeMapper;

    private final ScoreTypeRepository scoreTypeRepository;

    @Override
    public List<ScoreTypeDto> findAll() {
        return scoreTypeRepository.findAll().stream().map(scoreTypeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UUID> create(UUID id, ScoreTypeDto scoreTypeDto) {
        return Optional.ofNullable(scoreTypeRepository.save(scoreTypeMapper.toModel(scoreTypeDto)).getUid());
    }

    @Override
    public boolean delete(UUID id) {
        var scoreType = scoreTypeRepository.findByUid(id);
        if (scoreType.isEmpty()) {
            return false;
        }

        scoreTypeRepository.delete(scoreType.get());
        return true;
    }
}
