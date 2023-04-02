package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.PackageType;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.common.dto.StandardTypeMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(uses = {
        JobDetailMapper.class,
        BookingMapper.class,
        CategoryMapper.class,
        EntityMapper.class,
        LocationMapper.class,
        StandardTypeMapper.class,
},
    config = MappingConfig.class)
public abstract class PackageMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "archived"})
    @Mapping(target = "talentId", source = "talent", qualifiedBy = EntityMapper.ToTalentUid.class)
    @Mapping(target = "talentName", source = "talent", qualifiedBy = EntityMapper.ToTalentName.class)
    @Mapping(target = "orderNum", source = "orders", qualifiedByName = "toOrderNum")
    @Mapping(target = "packageType", source = "packageType", qualifiedBy = StandardTypeMapper.ToPackageTypeKey.class)
    public abstract ReadPackageDto toDto(Package talentPackage);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "archived", constant = "false")
    @Mapping(target = "packageType", source = ".", qualifiedByName = "toPackageType")
    public abstract Package fromCreateDtoToModel(CreatePackageDto createPackageDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "archived", constant = "false")
    @Mapping(target = "packageType", ignore = true)
    public abstract Package fromUpdateDtoToModel(UpdatePackageDto updatePackageDto);

    @Named("toOrderNum")
    public Integer toOrderNum(Set<Booking> orders) {
        return orders.size();
    }

    @Named("toPackageType")
    public PackageType toPackageType(CreatePackageDto dto) {
        return dto.getRepeatPattern() != null ? PackageType.RECURRING : PackageType.ONCE;
    }
}
