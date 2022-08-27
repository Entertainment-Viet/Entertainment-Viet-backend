package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TalentService implements TalentBoundary {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    @Override
    public Optional<TalentDto> findByUid(UUID uid) {
        return talentRepository.findByUid(uid).map(talentMapper::toDto);
    }
    @Override
    public Optional<UUID> create(TalentDto talentDto) {
        talentDto.setUid(null);
        var newTalent = talentRepository.save(talentMapper.toModel(talentDto));
        return Optional.ofNullable(newTalent.getUid());
    }

    @Override
    public Optional<UUID> update(TalentDto talentDto, UUID uid){
        return talentRepository.findByUid(uid)
                .map(talent -> updateTalentInfo(talent, talentMapper.toModel(talentDto)))
                .map(talentRepository::save)
                .map(Identifiable::getUid);
    }

    private Talent updateTalentInfo(Talent talent, Talent newInfo) {
        talent.setPhoneNumber(newInfo.getPhoneNumber());
        talent.setEmail(newInfo.getEmail());
        talent.setAddress(newInfo.getAddress());
        talent.setBio(newInfo.getBio());
        talent.setExtensions(newInfo.getExtensions());
        talent.setDisplayName(newInfo.getDisplayName());
        return talent;
    }
}
