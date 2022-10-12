package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.TalentMapper;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TalentService implements TalentBoundary {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    @Override
    public CustomPage<ReadTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable) {
        var dataPage = RestUtils.toLazyLoadPageResponse(
            talentRepository.findAll(paramDto, pageable)
            .map(talentMapper::toDto)
            .map(talentMapper::checkPermission));

        if (talentRepository.findAll(paramDto, pageable.next()).hasContent()) {
            dataPage.getPaging().setLast(false);
        }

        return dataPage;
    }

    @Override
    public Optional<ReadTalentDto> findByUid(UUID uid) {
        return talentRepository.findByUid(uid).map(talentMapper::toDto).map(talentMapper::checkPermission);
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

        if (!EntityValidationUtils.isTalentWithUid(talent, uid)) {
            return false;
        }
        if (!talent.verifyAccount()) {
            return false;
        }
        talentRepository.save(talent);
        return true;
    }
}
