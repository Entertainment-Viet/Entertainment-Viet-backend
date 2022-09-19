package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.TalentMapper;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TalentService implements TalentBoundary {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    @Override
    public Page<ReadTalentDto> findAll(Pageable pageable) {
        return talentRepository.findAll(pageable).map(talentMapper::toDto);
    }

    @Override
    public Optional<ReadTalentDto> findByUid(UUID uid) {
        return talentRepository.findByUid(uid).map(talentMapper::toDto);
    }
    @Override
    public Optional<UUID> create(UpdateTalentDto createTalentDto, UUID uid) {

        var newTalent = talentMapper.toModel(createTalentDto);
        newTalent.setUid(uid);
        newTalent.setUserState(UserState.GUEST);

        return Optional.ofNullable(talentRepository.save(newTalent).getUid());
    }

    @Override
    public Optional<UUID> update(UpdateTalentDto updateTalentDto, UUID uid){
        return talentRepository.findByUid(uid)
                .map(talent -> talent.updateInfo(talentMapper.toModel(updateTalentDto)))
                .map(talentRepository::save)
                .map(Identifiable::getUid);
    }

    @Override
    @Transactional
    public boolean verify(UUID uid) {
        var talent = talentRepository.findByUid(uid).orElse(null);

        if (talent == null) {
            log.warn(String.format("Can not find organizer with id '%s'", uid));
            return false;
        }
        if (!talent.verifyAccount()) {
            return false;
        }
        talentRepository.save(talent);
        return true;
    }
}
