package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(uses = {
        JobDetailMapper.class,
        BookingMapper.class,
        CategoryMapper.class
    },
    config = MappingConfig.class)
public abstract class PackageMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "talent"})
    @Mapping(target = "talentId", source = "talent", qualifiedByName = "toTalentId")
    public abstract PackageDto toDto(Package talentPackage);

    @BeanMapping(ignoreUnmappedSourceProperties = {"talentId"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talent", ignore = true)
    public abstract Package toModel(PackageDto packageDto);

    @Named("toTalentId")
    public UUID toTalentId(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }
}
