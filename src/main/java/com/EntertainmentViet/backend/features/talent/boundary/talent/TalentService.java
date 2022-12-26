package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    public CustomPage<ReadTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable) {
        var dataPage = RestUtils.toLazyLoadPageResponse(
            talentRepository.findAll(paramDto, pageable)
                .map(talentMapper::toDto)
                .map(dto -> talentMapper.checkPermission(dto, false))
        );

        if (talentRepository.findAll(paramDto, pageable.next()).hasContent()) {
            dataPage.getPaging().setLast(false);
        }

        return dataPage;
    }

    @Override
    public Optional<ReadTalentDto> findByUid(UUID uid, boolean isOwnerUser) {
        return talentRepository.findByUid(uid).map(talentMapper::toDto).map(dto -> talentMapper.checkPermission(dto, isOwnerUser));
    }

    @Override
    public Optional<UUID> create(CreatedTalentDto createTalentDto, UUID uid) {

        var createdTalent = talentMapper.fromCreateDtoToModel(createTalentDto);
        createdTalent.setUid(uid);
        createdTalent.setUserState(UserState.GUEST);
        createdTalent.setAccountType(AccountType.TALENT);
        createdTalent.getTalentDetail().setTalent(createdTalent);
        createdTalent.setReviewSum(Collections.nCopies(5, 0));
        createdTalent.setArchived(false);

        return Optional.ofNullable(talentRepository.save(createdTalent).getUid());
    }

    @Override
    public Optional<UUID> update(UpdateTalentDto updateTalentDto, UUID uid){
        return talentRepository.findByUid(uid)
                .map(talent -> talent.updateInfo(talentMapper.fromUpdateDtoToModel(updateTalentDto)))
                .map(talentRepository::save)
                .map(Identifiable::getUid);
    }

    @Override
    public Optional<UUID> updateKyc(UpdateTalentKycInfoDto kycInfoDto, UUID uid) {
        return talentRepository.findByUid(uid)
            .map(talent -> talent.requestKycInfoChange(talentMapper.fromKycDtoToModel(kycInfoDto)))
            .map(talentRepository::save)
            .map(Identifiable::getUid);
    }

    @Override
    public boolean sendVerifyRequest(UUID uid) {
        var talent = talentRepository.findByUid(uid).orElse(null);

        if (!EntityValidationUtils.isTalentWithUid(talent, uid)) {
            return false;
        }
        if (!talent.sendVerifyRequest()) {
            return false;
        }
        talentRepository.save(talent);
        return true;
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
