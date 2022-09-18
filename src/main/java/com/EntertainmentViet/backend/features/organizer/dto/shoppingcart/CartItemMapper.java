package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ReadPackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.UpdatePackageDto;
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
public abstract class CartItemMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "orders"})
    @Mapping(target = "talentId", source = "talent", qualifiedByName = "toTalentId")
    public abstract ReadCartItemDto toDto(Package talentPackage);

    @Named("toTalentId")
    public UUID toTalentId(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }

}
