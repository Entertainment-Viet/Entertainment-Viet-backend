package com.EntertainmentViet.backend.features.booking.dto.locationaddress;

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
public class LocationAddressDto extends IdentifiableDto {

	@NotNull
	private String city;

	private String district;

	private String street;

}
