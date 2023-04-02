package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.common.dto.StandardTypeMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
    JobDetailMapper.class,
    BookingMapper.class,
    CategoryMapper.class,
    EntityMapper.class,
    StandardTypeMapper.class
},
    config = MappingConfig.class)
public abstract class CartItemMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "organizer"})
    @Mapping(target = "talentId", source = "talentPackage.talent", qualifiedBy = EntityMapper.ToTalentUid.class)
    @Mapping(target = "talentName", source = "talentPackage.talent", qualifiedBy = EntityMapper.ToTalentName.class)
    @Mapping(target = "name", source = "talentPackage.name")
    @Mapping(target = "isValid", source = ".", qualifiedByName = "toValid")
    @Mapping(target = "suggestedPrice", source = "price")
    @Mapping(target = "jobDetail", source = "talentPackage.jobDetail")
    @Mapping(target = "packageType", source = "talentPackage.packageType", qualifiedBy = StandardTypeMapper.ToPackageTypeKey.class)
    @Mapping(target = "repeatPattern", source = "talentPackage.repeatPattern")
    public abstract ReadCartItemDto toDto(OrganizerShoppingCart talentPackage);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "talentPackage", ignore = true)
    @Mapping(target = "price", source = "suggestedPrice")
    public abstract OrganizerShoppingCart fromUpdateDtoToModel(UpdateCartItemDto dto);

    @Named("toValid")
    public Boolean toValid(OrganizerShoppingCart cartItem) {
        return cartItem.checkValidCartItem();
    }
}
