package com.EntertainmentViet.backend.features.finance.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.Account;
import com.EntertainmentViet.backend.domain.entities.finance.UserDealFeeRate;
import com.EntertainmentViet.backend.features.finance.dao.UserDealFeeRateRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper(config = MappingConfig.class)
public abstract class UserDealFeeRateMapper {

    @Autowired
    private UserDealFeeRateRepository userDealFeeRateRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "user"})
    public abstract UserDealFeeRateDto toReadDto(UserDealFeeRate userDealFeeRate);

    @ToCustomFeeRate
    public UserDealFeeRateDto toCustomFeeRate(Account account) {
        return toReadDto(userDealFeeRateRepository.findByUserUid(account.getUid()).orElse(null));
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToCustomFeeRate {  }
}
