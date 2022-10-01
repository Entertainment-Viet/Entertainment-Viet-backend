package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
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
public abstract class CartItemMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "organizer"})
    @Mapping(target = "talentId", source = "talentPackage.talent", qualifiedByName = "toTalentId")
    @Mapping(target = "name", source = "talentPackage.name")
    @Mapping(target = "isActive", source = "talentPackage.isActive")
    @Mapping(target = "suggestedPrice", source = "price")
    @Mapping(target = "jobDetail", source = "talentPackage.jobDetail")
    public abstract ReadCartItemDto toDto(OrganizerShoppingCart talentPackage);

    @Named("toTalentId")
    public UUID toTalentId(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }

}
