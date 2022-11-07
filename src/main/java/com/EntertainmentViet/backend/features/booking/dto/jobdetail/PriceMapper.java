package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.values.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;

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

    @ToReadDto
    public PriceDto toReadDto(Price price) {
        return PriceDto.builder()
                .currency(toCurrencyKey(price.getCurrency()))
                .max(price.getMax())
                .min(price.getMin())
                .build();
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToReadDto {

    }
}
