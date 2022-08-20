package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.Price;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class PriceMapper {

    @Mapping(target = "currency", source = "currency", qualifiedByName = "toCurrencyKey")
    public abstract PriceDto toDto(Price price);

    @Mapping(target = "currency", source = "currency", qualifiedByName = "toCurrency")
    public abstract Price toModel(PriceDto priceDto);

    @Named("toCurrencyKey")
    public String toCurrencyKey(Currency currency) {
        return currency != null ? currency.i18nKey : null;
    }

    @Named("toCurrency")
    public Currency toCurrency(String key) {
        return Currency.ofI18nKey(key);
    }
}
