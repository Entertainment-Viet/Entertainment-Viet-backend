package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
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

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "talent", source = "talent", qualifiedByName = "toTalentUid")
    @Mapping(target = "comment", source = "comment", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    public abstract ReviewDto toDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talent", source = "talent", qualifiedByName = "toTalentEntity")
    @Mapping(target = "comment", source = "comment", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Review toModel(ReviewDto dto);

    @Named("toTalentUid")
    public UUID toTalentUid(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }

    @Named("toTalentEntity")
    public Talent toTalentEntity(UUID talentUid) {
        return talentRepository.findByUid(talentUid).orElse(null);
    }
}
