package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

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

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "orders"})
    @Mapping(target = "talentId", source = "talent", qualifiedByName = "toTalentId")
    public abstract ReadPackageDto toDto(Package talentPackage);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "orders", ignore = true)
    public abstract Package fromCreateDtoToModel(CreatePackageDto createPackageDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "orders", ignore = true)
    public abstract Package fromUpdateDtoToModel(UpdatePackageDto updatePackageDto);

    @Named("toTalentId")
    public UUID toTalentId(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }
}
