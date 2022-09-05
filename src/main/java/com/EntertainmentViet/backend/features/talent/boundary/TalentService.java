package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TalentService implements TalentBoundary {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    @Override
    public Optional<TalentDto> findByUid(UUID uid) {
        return talentRepository.findByUid(uid).map(talentMapper::toDto);
    }
    @Override
    public Optional<UUID> create(TalentDto talentDto, UUID uid) {
        // TODO add check not exist username

        var newTalent = talentMapper.toModel(talentDto);
        newTalent.setUid(uid);
        newTalent.setId(null);
        newTalent.setReviews(Collections.emptyList());
        newTalent.setBookings(Collections.emptyList());
        newTalent.setFeedbacks(Collections.emptyList());
        newTalent.setUserState(UserState.GUEST);

        return Optional.ofNullable(talentRepository.save(newTalent).getUid());
    }

    @Override
    public Optional<UUID> update(TalentDto talentDto, UUID uid){
        return talentRepository.findByUid(uid)
                .map(talent -> updateTalentInfo(talent, talentMapper.toModel(talentDto)))
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
