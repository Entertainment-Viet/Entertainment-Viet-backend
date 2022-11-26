package com.EntertainmentViet.backend.features.booking.dto.location;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.features.booking.dao.location.LocationRepository;
import com.EntertainmentViet.backend.features.booking.dao.location.LocationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {
				LocationTypeMapper.class
}, config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class LocationMapper {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private LocationTypeRepository locationTypeRepository;

	@BeanMapping(ignoreUnmappedSourceProperties = { "id" })
	@Mapping(target = "coordinate", source = "coordinate", qualifiedByName = "toGeometryText")
	@Mapping(target = "boundary", source = "boundary", qualifiedByName = "toGeometryText")
	@Mapping(target = "parentName", source = "parent", qualifiedByName = "toParentName")
	@Mapping(target = "parentUid", source = "parent", qualifiedByName = "toParentUid")
	@Mapping(target = "locationType", source = "type")
	public abstract LocationDto toDto(Location location);

	@BeanMapping(ignoreUnmappedSourceProperties = {"parentName"})
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "coordinate", source = "coordinate", qualifiedByName = "toGeometry")
	@Mapping(target = "boundary", source = "boundary", qualifiedByName = "toGeometry")
	@Mapping(target = "parent", source = "parentUid", qualifiedByName = "toParentLocation")
	@Mapping(target = "type", source = "locationType")
	public abstract Location toModel(LocationDto locationDto);

	public Location fromInputToModel(InputLocationDto locationDto) {
		if (locationDto == null) {
			return null;
		}

		return Location.builder()
				.type(locationTypeRepository.findById(1L).orElseThrow())
				.name(locationDto.getAddress())
				.parent(locationRepository.findByUid(locationDto.getParentId()).orElse(null))
				.build();
	}

	public ReadLocationDto fromModelToReadDto(Location location) {
		if (location == null) {
			return null;
		}
		return ReadLocationDto.builder()
				.uid(location.getUid())
				.name(location.getName())
				.type(location.getType().getType())
				.level(location.getType().getLevel())
				.parent(location.getParent() != null ? fromModelToReadDto(location.getParent()) : null)
				.build();
	}

	@ToLocation
	public Location toModel(UUID locationUid) {
		if (locationUid == null) {
			return null;
		}
		return locationRepository.findByUid(locationUid).orElse(null);
	}

	@Qualifier
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.CLASS)
	public @interface ToLocation {

	}

	@Named("toParentName")
	public String toParentName(Location location) {
		return location != null ? location.getName() : null;
	}

	@Named("toParentUid")
	public UUID toParentUid(Location location) {
		return location != null ? location.getUid() : null;
	}

	@Named("toParentLocation")
	public Location toParentLocation(UUID uid) {
		if (uid == null) {
			return null;
		}
		return locationRepository.findByUid(uid).orElse(null);
	}

	@Named("toGeometryText")
	public String toGeometryText(Geometry geometry) {
		return "";
	}

	@Named("toGeometry")
	public Geometry toGeometry(String geometryText) {
		return null;
	}
}
