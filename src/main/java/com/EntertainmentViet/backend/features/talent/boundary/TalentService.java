package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TalentService implements TalentBoundary {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    @Override
    public List<TalentDto> findAll() {
        return talentRepository.findAll().stream().map(talentMapper::toDto).toList();
    }

    @Override
    public TalentDto findByUid(UUID uid) {
        return talentMapper.toDto(talentRepository.findByUid(uid).orElse(null));
    }
}
