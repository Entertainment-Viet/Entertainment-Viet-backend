package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
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
        UserInputTextMapper.class,
        EntityMapper.class
    },
    config = MappingConfig.class)
public abstract class ReviewMapper {


    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "talentId", source = "talent", qualifiedBy = EntityMapper.ToTalentUid.class)
    @Mapping(target = "talentName", source = "talent", qualifiedBy = EntityMapper.ToTalentName.class)
    @Mapping(target = "organizerId", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerUid.class)
    @Mapping(target = "organizerName", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerName.class)
    @Mapping(target = "comment", source = "comment", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    public abstract ReadReviewDto toDto(Review review);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "organizer", source = "organizerId", qualifiedBy = EntityMapper.ToOrganizerEntity.class)
    @Mapping(target = "talent", source = "talentId", qualifiedBy = EntityMapper.ToTalentEntity.class)
    @Mapping(target = "comment", source = "comment", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Review fromCreateToModel(CreateReviewDto dto);
}
