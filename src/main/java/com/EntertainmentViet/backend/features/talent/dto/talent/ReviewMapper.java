package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = {
        UserInputTextMapper.class
    },
    config = MappingConfig.class)
public abstract class ReviewMapper {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "talent", source = "talent", qualifiedByName = "toTalentUid")
    @Mapping(target = "organizer", source = "organizer", qualifiedByName = "toOrganizerUid")
    @Mapping(target = "comment", source = "comment", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    public abstract ReadReviewDto toDto(Review review);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "organizer", source = "organizer", qualifiedByName = "toOrganizerEntity")
    @Mapping(target = "talent", source = "talent", qualifiedByName = "toTalentEntity")
    @Mapping(target = "comment", source = "comment", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Review fromCreateToModel(CreateReviewDto dto);

    @Named("toTalentUid")
    public UUID toTalentUid(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }

    @Named("toTalentEntity")
    public Talent toTalentEntity(UUID talentUid) {
        return talentRepository.findByUid(talentUid).orElse(null);
    }

    @Named("toOrganizerUid")
    public UUID toOrganizerUid(Organizer organizer) {
        return organizer != null ? organizer.getUid() : null;
    }

    @Named("toOrganizerEntity")
    public Organizer toOrganizerEntity(UUID organizerUid) {
        return organizerRepository.findByUid(organizerUid).orElse(null);
    }
}
