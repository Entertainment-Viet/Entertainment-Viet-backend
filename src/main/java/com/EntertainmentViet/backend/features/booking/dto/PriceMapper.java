package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.Price;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class PriceMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"currency"})
    // TODO adding currency convert
    public abstract PriceDto toDto(Price price);

    @Mapping(target = "currency", ignore = true)
    public abstract Price toModel(PriceDto priceDto);

}
