/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class QueryParamsUtils {
	public boolean isCurrencyNotProvided(ListEventParamDto paramDto) {
		return paramDto.getCurrency() == null && (paramDto.getMaxPrice() != null || paramDto.getMinPrice() != null);
	}

	public boolean isCurrencyNotProvided(ListTalentParamDto paramDto) {
		return paramDto.getCurrency() == null && (paramDto.getMaxPrice() != null || paramDto.getMinPrice() != null);
	}

	public boolean isCurrencyNotProvided(ListPackageParamDto paramDto) {
		return paramDto.getCurrency() == null && (paramDto.getMaxPrice() != null || paramDto.getMinPrice() != null);
	}
}
