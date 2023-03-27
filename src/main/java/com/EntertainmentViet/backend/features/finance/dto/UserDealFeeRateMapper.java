package com.EntertainmentViet.backend.features.finance.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.finance.UserDealFeeRate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class UserDealFeeRateMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "user"})
    public abstract UserDealFeeRateDto toReadDto(UserDealFeeRate userDealFeeRate);
}
