package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TalentService implements TalentBoundary {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    @Override
    public TalentDto findByUid(UUID uid) {
        return talentMapper.toDto(talentRepository.findByUid(uid).orElse(null));
    }
    @Override
    public TalentDto create(TalentDto talentDto) {
        var newTalent = talentRepository.save(talentMapper.toModel(talentDto));
        return talentMapper.toDto(newTalent);
    }

    @Override
    public TalentDto update(TalentDto talentDto, UUID uid) throws Exception {
        Talent talentCheck = talentRepository.findByUid(uid).orElse(null);
        if (talentCheck != null) {
            Talent talentUpdate = talentMapper.toModel(talentDto);
            talentCheck.setDisplayName(talentUpdate.getDisplayName());
            talentCheck.setPhoneNumber(talentUpdate.getPhoneNumber());
            talentCheck.setEmail(talentUpdate.getEmail());
            talentCheck.setBio(talentUpdate.getBio());
            talentCheck.setExtensions(talentUpdate.getExtensions());
            talentCheck.setUserState(talentUpdate.getUserState());

            var updated = talentRepository.save(talentCheck);
            return talentMapper.toDto(updated);
        }
        throw new Exception();
    }
}
