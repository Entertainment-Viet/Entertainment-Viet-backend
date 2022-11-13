package com.EntertainmentViet.backend.features.booking.dto.locationaddress;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.LocationAddress;
import com.EntertainmentViet.backend.features.booking.dao.locationaddress.LocationAddressRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class LocationAddressMapper {

	@Autowired
	private LocationAddressRepository locationAddressRepository;

	@BeanMapping(ignoreUnmappedSourceProperties = { "id", "coordinates" })
	public abstract LocationAddressDto toDto(LocationAddress locationAddress);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "coordinates", ignore = true)
	public abstract LocationAddress toModel(LocationAddressDto locationAddressDto);

	@ToLocationAddress
	public LocationAddress toLocationAddress(UUID locationAddressUid) {
		if (locationAddressUid == null) {
			return null;
		}
		return locationAddressRepository.findByUid(locationAddressUid).orElse(null);
	}

	@Qualifier
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.CLASS)
	public @interface ToLocationAddress {

	}
}
