package com.EntertainmentViet.backend.features.booking.dto.location;

import java.util.UUID;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class LocationDto extends IdentifiableDto {

	private LocationTypeDto locationType;

	@NotNull
	private String name;

	private String nameCode;

	private String phoneCode;

	private String zipcode;

	private String coordinate;

	private String boundary;

	private String parentName;

	private UUID parentUid;
}
