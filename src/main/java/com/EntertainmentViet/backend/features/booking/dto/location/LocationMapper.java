package com.EntertainmentViet.backend.features.booking.dto.location;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.features.booking.dao.location.LocationRepository;
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

	@BeanMapping(ignoreUnmappedSourceProperties = { "id" })
	@Mapping(target = "coordinate", source = "coordinate", qualifiedByName = "toGeometryText")
	@Mapping(target = "boundary", source = "boundary", qualifiedByName = "toGeometryText")
	@Mapping(target = "parentName", source = "parent", qualifiedByName = "toParentName")
	@Mapping(target = "parentUid", source = "parent", qualifiedByName = "toParentUid")
	public abstract LocationDto toDto(Location location);

	@BeanMapping(ignoreUnmappedSourceProperties = {"parentName"})
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "coordinate", source = "coordinate", qualifiedByName = "toGeometry")
	@Mapping(target = "boundary", source = "boundary", qualifiedByName = "toGeometry")
	@Mapping(target = "parent", source = "parentUid", qualifiedByName = "toParentLocation")
	public abstract Location toModel(LocationDto locationDto);

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
