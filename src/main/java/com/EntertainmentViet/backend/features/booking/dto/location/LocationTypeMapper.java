/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.features.booking.dto.location;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.LocationType;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class LocationTypeMapper {

	@BeanMapping(ignoreUnmappedSourceProperties = { "id" })
	public abstract LocationTypeDto toDto(LocationType locationType);

	@Mapping(target = "id", ignore = true)
	public abstract LocationType toModel(LocationTypeDto locationDto);
}
