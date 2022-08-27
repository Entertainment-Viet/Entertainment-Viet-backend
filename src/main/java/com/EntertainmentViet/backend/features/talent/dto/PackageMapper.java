package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        JobDetailMapper.class,
        BookingMapper.class
    },
    config = MappingConfig.class)
public abstract class PackageMapper {

    // TODO fix this
    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "talent"})
    @Mapping(target = "talentId", ignore = true)
    public abstract PackageDto toDto(Package talentPackage);
}
