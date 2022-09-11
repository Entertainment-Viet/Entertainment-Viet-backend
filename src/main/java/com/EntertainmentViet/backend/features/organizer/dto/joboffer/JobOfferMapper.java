package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = {JobDetailMapper.class}, config = MappingConfig.class)
public abstract class JobOfferMapper {

    @Autowired
    private OrganizerRepository organizerRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "organizerId", source = "organizer", qualifiedByName = "toOrganizerUid")
    public abstract ReadJobOfferDto toDto(JobOffer jobOffer);

    // TODO REMOVE THIS
    @BeanMapping(ignoreUnmappedSourceProperties = {"organizerId"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    public abstract JobOffer toModel(ReadJobOfferDto jobDetailDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "organizer", source = "organizerId", qualifiedByName = "toOrganizerEntity")
    public abstract JobOffer fromCreateDtoToModel(CreateJobOfferDto createJobOfferDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "organizer", source = "organizerId", qualifiedByName = "toOrganizerEntity")
    public abstract JobOffer fromUpdateDtoToModel(UpdateJobOfferDto updateJobOfferDto);


    @Named("toOrganizerUid")
    public UUID toOrganizerUid(Organizer organizer) {
        return organizer != null ? organizer.getUid() : null;
    }

    @Named("toOrganizerEntity")
    public Organizer toOrganizerEntity(UUID organizerUid) {
        return organizerRepository.findByUid(organizerUid).orElse(null);
    }
}
