package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class QueryParamsUtils {

	public boolean isInvalidParams(ListEventParamDto paramDto) {
		return currencyNotProvided(paramDto.getCurrency(), paramDto.getMaxPrice(), paramDto.getMinPrice()) ||
						priceRangeNotValid(paramDto.getMaxPrice(), paramDto.getMinPrice());
	}

	public boolean isInvalidParams(ListTalentParamDto paramDto) {
		return currencyNotProvided(paramDto.getCurrency(), paramDto.getMaxPrice(), paramDto.getMinPrice()) ||
						priceRangeNotValid(paramDto.getMaxPrice(), paramDto.getMinPrice());
	}

	public boolean isInvalidParams(ListPackageParamDto paramDto) {
		return currencyNotProvided(paramDto.getCurrency(), paramDto.getMaxPrice(), paramDto.getMinPrice()) ||
						priceRangeNotValid(paramDto.getMaxPrice(), paramDto.getMinPrice());
	}

	public boolean currencyNotProvided(String currencyI18n, Double maxPrice, Double minPrice) {
		return currencyI18n == null && (maxPrice != null || minPrice != null);
	}

	public boolean priceRangeNotValid(Double maxPrice, Double minPrice) {
		return maxPrice != null && minPrice != null && maxPrice < minPrice;
	}
}
