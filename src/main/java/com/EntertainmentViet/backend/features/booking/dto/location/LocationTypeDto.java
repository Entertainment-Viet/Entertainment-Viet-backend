/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.features.booking.dto.location;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class LocationTypeDto {

	@NotNull
	private String type;

	@NotNull
	private Long level;

}
